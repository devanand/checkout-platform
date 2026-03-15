package com.haiilo.checkout.domain;

import com.haiilo.checkout.pricing.model.PricingRequest;

import java.util.Objects;

public record CartItem(
        String productId,
        int quantity
) {
    public CartItem {
        Objects.requireNonNull(productId, "productId must not be null");

        if (productId.isBlank()) {
            throw new IllegalArgumentException("productId must not be blank");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }
    }

    public PricingRequest toPricingRequest(String currency) {
        return new PricingRequest(
                productId,
                quantity,
                currency
        );
    }
}