package com.haiilo.checkout.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CheckoutItemRequest(
        @NotBlank String productId,
        @Min(1) int quantity
) {}
