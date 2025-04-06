package com.kaizenflow.bookquik.booking.service;

import com.kaizenflow.bookquik.booking.domain.entity.Customer;
import com.kaizenflow.bookquik.booking.repository.CustomerRepository;
import com.kaizenflow.bookquik.booking.repository.OrderRepository;
import org.springframework.stereotype.Service;

import com.kaizenflow.bookquik.booking.domain.request.BookingRequest;
import com.kaizenflow.bookquik.booking.domain.response.BookingResponse;

import java.util.Optional;

@Service
public class BookingService {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public BookingService(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public BookingResponse createBooking(final BookingRequest bookingRequest) {
        final Customer customer = customerRepository.findById(bookingRequest.getUserId()).orElseThrow(RuntimeException::new);
        return BookingResponse.builder().build();
    }
}
