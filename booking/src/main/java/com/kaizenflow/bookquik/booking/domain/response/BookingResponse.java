package com.kaizenflow.bookquik.booking.domain.response;

import java.math.BigDecimal;

public record BookingResponse(
        Long userId, Long eventId, Long ticketCount, BigDecimal ticketPrice) {}
