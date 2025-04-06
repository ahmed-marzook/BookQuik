package com.kaizenflow.bookquik.booking.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaizenflow.bookquik.booking.domain.request.BookingRequest;
import com.kaizenflow.bookquik.booking.domain.response.BookingResponse;
import com.kaizenflow.bookquik.booking.service.BookingService;

@RestController
@RequestMapping("/api/v1")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book")
    public BookingResponse createBooking(@RequestBody BookingRequest bookingRequest)
            throws Exception {
        return bookingService.createBooking(bookingRequest);
    }
}
