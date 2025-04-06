package com.kaizenflow.bookquik.inventory.domain.response;

import com.kaizenflow.bookquik.inventory.domain.entity.Venue;

public record EventInventoryResponse(
        String name, Long totalCapacity, Long leftCapacity, Venue venue) {}
