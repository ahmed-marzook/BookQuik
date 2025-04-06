package com.kaizenflow.bookquik.booking.service;

import java.math.BigDecimal;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.kaizenflow.bookquik.booking.client.InventoryServiceClient;
import com.kaizenflow.bookquik.booking.domain.entity.Customer;
import com.kaizenflow.bookquik.booking.domain.event.BookingEvent;
import com.kaizenflow.bookquik.booking.domain.request.BookingRequest;
import com.kaizenflow.bookquik.booking.domain.response.BookingResponse;
import com.kaizenflow.bookquik.booking.domain.response.InventoryResponse;
import com.kaizenflow.bookquik.booking.repository.CustomerRepository;
import com.kaizenflow.bookquik.booking.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookingService {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    public BookingService(
            CustomerRepository customerRepository,
            OrderRepository orderRepository,
            InventoryServiceClient inventoryServiceClient,
            KafkaTemplate<String, BookingEvent> kafkaTemplate) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public BookingResponse createBooking(final BookingRequest bookingRequest) {
        final Customer customer =
                customerRepository.findById(bookingRequest.getUserId()).orElseThrow(RuntimeException::new);
        final InventoryResponse inventoryResponse =
                inventoryServiceClient.getInventory(bookingRequest.getEventId());
        if (inventoryResponse.leftCapacity() < bookingRequest.getTicketCount()) {
            throw new RuntimeException("Ticket count exceeded");
        }

        final BookingEvent bookingEvent =
                createBookingEvent(bookingRequest, customer, inventoryResponse);
        kafkaTemplate.send("booking", bookingEvent);
        log.info("Booking sent to kafka: {}", bookingEvent);
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
