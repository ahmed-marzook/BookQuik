package com.kaizenflow.bookquik.booking.controller;

import com.kaizenflow.bookquik.booking.service.BookingService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public BookingResponse createBooking(@RequestBody BookingRequest bookingRequest) {
        return bookingService.createBooking(bookingRequest);
    }
}
