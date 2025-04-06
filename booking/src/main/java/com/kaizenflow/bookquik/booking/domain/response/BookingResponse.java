package com.kaizenflow.bookquik.booking.domain.response;

public record BookingResponse(
        Long bookingId, Long userId, Long eventId, Long ticketCount, String ticketPrice) {}
