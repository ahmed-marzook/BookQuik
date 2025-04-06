package com.kaizenflow.bookquik.booking.domain.response;

import java.math.BigDecimal;

public record InventoryResponse(
        Long eventId,
        String name,
        Long totalCapacity,
        Long leftCapacity,
        VenueResponse venue,
        BigDecimal ticketPrice) {}
