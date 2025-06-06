package com.kaizenflow.bookquik.inventory.domain.response;

import java.math.BigDecimal;

public record EventInventoryResponse(
        Long eventId,
        String name,
        Long totalCapacity,
        Long leftCapacity,
        VenueInventoryResponse venue,
        BigDecimal ticketPrice) {}
