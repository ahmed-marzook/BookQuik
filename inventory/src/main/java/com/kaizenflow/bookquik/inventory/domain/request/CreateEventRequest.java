package com.kaizenflow.bookquik.inventory.domain.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateEventRequest(
        @NotBlank(message = "Event name is required") String name,
        @NotNull(message = "Total capacity is required")
                @Positive(message = "Total capacity must be positive")
                Long totalCapacity,
        @NotNull(message = "Venue ID is required") Long venueId,
        @NotNull(message = "Ticket price is required")
                @Positive(message = "Ticket price must be positive")
                BigDecimal ticketPrice) {}
