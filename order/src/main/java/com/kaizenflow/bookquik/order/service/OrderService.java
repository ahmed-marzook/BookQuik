package com.kaizenflow.bookquik.order.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

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

    @KafkaListener(topics = "booking", groupId = "order-service")
    public void orderEvent(BookingEvent bookingEvent) {
        log.info("Booking event received: {}", bookingEvent);
        Order order = createOrder(bookingEvent);
        orderRepository.saveAndFlush(order);
        inventoryServiceClient.updateInventory(order.getEventId(), order.getTicketCount());
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
