package com.kaizenflow.bookquik.booking.service;

import org.springframework.stereotype.Service;

import com.kaizenflow.bookquik.booking.domain.request.BookingRequest;
import com.kaizenflow.bookquik.booking.domain.response.BookingResponse;

@Service
public class BookingService {
    public BookingResponse createBooking(final BookingRequest bookingRequest) {
        return BookingResponse.builder().build();
    }
}
