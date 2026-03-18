package com.haiilo.checkout.application;

import com.haiilo.checkout.api.money.Money;
import com.haiilo.checkout.api.pricing.PricingResponse;

import java.util.Objects;

public record CheckoutLine(
        String productId,
        int quantity,
        Money unitPrice,
        Money lineTotal,
        String offerType,
        String offerDescription,
        boolean offerApplied) {
    public CheckoutLine {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");
        Objects.requireNonNull(lineTotal, "lineTotal must not be null");

        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }
    }

    public static CheckoutLine fromPricingResponse(PricingResponse response) {
        return new CheckoutLine(
                response.productId(),
                response.quantity(),
                response.unitPrice(),
                response.lineTotal(),
                response.offerType(),
                response.offerDescription(),
                response.offerApplied());
    }
}