package com.kaizenflow.bookquik.inventory.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateVenueRequest(
        @NotBlank(message = "Venue name is required") String name,
        @NotNull(message = "Total capacity is required")
                @Positive(message = "Total capacity must be positive")
                Long totalCapacity,
        @NotBlank(message = "Address is required") String address) {}
