package com.kaizenflow.bookquik.booking.service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kaizenflow.bookquik.booking.client.InventoryServiceClient;
import com.kaizenflow.bookquik.booking.domain.entity.Customer;
import com.kaizenflow.bookquik.booking.domain.event.BookingEvent;
import com.kaizenflow.bookquik.booking.domain.request.BookingRequest;
import com.kaizenflow.bookquik.booking.domain.response.BookingResponse;
import com.kaizenflow.bookquik.booking.domain.response.InventoryResponse;
import com.kaizenflow.bookquik.booking.exception.KafkaPublishException;
import com.kaizenflow.bookquik.booking.exception.ResourceNotFoundException;
import com.kaizenflow.bookquik.booking.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookingService {
    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;
    private final long kafkaTimeout;

    public BookingService(
            CustomerRepository customerRepository,
            InventoryServiceClient inventoryServiceClient,
            KafkaTemplate<String, BookingEvent> kafkaTemplate,
            @Value("${spring.kafka.producer.timeout:5000}") long kafkaTimeout) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTimeout = kafkaTimeout;
    }

    @Transactional
    public BookingResponse createBooking(final BookingRequest bookingRequest) {
        log.info(
                "Creating booking for user ID: {} for event ID: {}",
                bookingRequest.getUserId(),
                bookingRequest.getEventId());

        final Customer customer =
                customerRepository
                        .findById(bookingRequest.getUserId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Customer", "id", bookingRequest.getUserId()));

        final InventoryResponse inventoryResponse =
                inventoryServiceClient.getInventory(bookingRequest.getEventId());

        if (inventoryResponse.leftCapacity() < bookingRequest.getTicketCount()) {
            throw new IllegalArgumentException(
                    String.format(
                            "Not enough tickets available. Requested: %d, Available: %d",
                            bookingRequest.getTicketCount(), inventoryResponse.leftCapacity()));
        }

        final BookingEvent bookingEvent =
                createBookingEvent(bookingRequest, customer, inventoryResponse);

        // Send to Kafka and wait for confirmation
        try {
            CompletableFuture<SendResult<String, BookingEvent>> future =
                    kafkaTemplate.send("booking", bookingEvent);

            // Wait for confirmation with timeout
            SendResult<String, BookingEvent> result = future.get(kafkaTimeout, TimeUnit.MILLISECONDS);

            // Log successful message delivery with partition and offset info
            log.info(
                    "Booking successfully sent to Kafka topic: {}, partition: {}, offset: {}",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaPublishException("Interrupted while sending booking to Kafka", e);
        } catch (ExecutionException e) {
            throw new KafkaPublishException("Error sending booking to Kafka: " + e.getMessage(), e);
        } catch (TimeoutException e) {
            throw new KafkaPublishException("Timeout waiting for Kafka confirmation", e);
        }

        return new BookingResponse(
                bookingEvent.getUserId(),
                bookingEvent.getEventId(),
                bookingEvent.getTicketCount(),
                bookingEvent.getTotalPrice());
    }

    private BookingEvent createBookingEvent(
            BookingRequest bookingRequest, Customer customer, InventoryResponse inventoryResponse) {
        return BookingEvent.builder()
                .userId(customer.getId())
                .eventId(bookingRequest.getEventId())
                .ticketCount(bookingRequest.getTicketCount())
                .totalPrice(
                        inventoryResponse
                                .ticketPrice()
                                .multiply(new BigDecimal(bookingRequest.getTicketCount())))
                .build();
    }
}
