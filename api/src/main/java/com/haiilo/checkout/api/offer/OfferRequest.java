package com.haiilo.checkout.api.offer;

import com.haiilo.checkout.api.money.Money;

import java.util.Objects;

public record OfferRequest(
        String productId,
        int quantity,
        Money unitPrice
) {
    public OfferRequest {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");

        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }
    }
}