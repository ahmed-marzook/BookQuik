package com.kaizenflow.bookquik.booking.service;

import org.springframework.stereotype.Service;

import com.kaizenflow.bookquik.booking.client.InventoryServiceClient;
import com.kaizenflow.bookquik.booking.domain.entity.Customer;
import com.kaizenflow.bookquik.booking.domain.request.BookingRequest;
import com.kaizenflow.bookquik.booking.domain.response.BookingResponse;
import com.kaizenflow.bookquik.booking.domain.response.InventoryResponse;
import com.kaizenflow.bookquik.booking.repository.CustomerRepository;
import com.kaizenflow.bookquik.booking.repository.OrderRepository;

@Service
public class BookingService {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final InventoryServiceClient inventoryServiceClient;

    public BookingService(
            CustomerRepository customerRepository,
            OrderRepository orderRepository,
            InventoryServiceClient inventoryServiceClient) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    public BookingResponse createBooking(final BookingRequest bookingRequest) {
        final Customer customer =
                customerRepository.findById(bookingRequest.getUserId()).orElseThrow(RuntimeException::new);
        final InventoryResponse inventoryResponse =
                inventoryServiceClient.getInventory(bookingRequest.getEventId());
        return new BookingResponse(null, null, null, null, null);
    }
}
