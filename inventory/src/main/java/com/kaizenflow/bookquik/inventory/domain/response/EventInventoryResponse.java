package com.kaizenflow.bookquik.inventory.domain.response;

public record EventInventoryResponse(
        Long eventId,
        String name,
        Long totalCapacity,
        Long leftCapacity,
        VenueInventoryResponse venue) {}
