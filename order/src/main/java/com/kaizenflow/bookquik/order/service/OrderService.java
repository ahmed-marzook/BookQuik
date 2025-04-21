package com.kaizenflow.bookquik.order.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kaizenflow.bookquik.order.client.InventoryServiceClient;
import com.kaizenflow.bookquik.order.domain.entity.Order;
import com.kaizenflow.bookquik.order.domain.event.BookingEvent;
import com.kaizenflow.bookquik.order.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryServiceClient inventoryServiceClient;

    public OrderService(
            OrderRepository orderRepository, InventoryServiceClient inventoryServiceClient) {
        this.orderRepository = orderRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @KafkaListener(topics = "booking", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void processBookingEvent(BookingEvent bookingEvent, Acknowledgment acknowledgment) {
        log.info("Booking event received: {}", bookingEvent);

        try {
            // Create and save order
            Order order = createOrder(bookingEvent);
            orderRepository.saveAndFlush(order);
            log.info("Order created with ID: {}", order.getId());

            // Update inventory
            inventoryServiceClient.updateInventory(order.getEventId(), order.getTicketCount());
            log.info("Inventory updated for event ID: {} with ticket count: {}",
                    order.getEventId(), order.getTicketCount());

            // Acknowledge the message after successful processing
            acknowledgment.acknowledge();
            log.info("Booking event acknowledged: {}", bookingEvent);
        } catch (Exception e) {
            log.error("Error processing booking event: {}", e.getMessage(), e);
            // Not acknowledging the message will cause it to be redelivered
            throw e; // Re-throw to trigger the error handler
        }
    }

    private Order createOrder(BookingEvent bookingEvent) {
        return Order.builder()
                .customerId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }
}