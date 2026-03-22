package com.haiilo.checkout.api.pricing;

import java.util.Objects;

public record PricingRequest(
        String productId,
        int quantity,
        String currency) {
    public PricingRequest {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(currency, "currency must not be null");

        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }
    }
}
