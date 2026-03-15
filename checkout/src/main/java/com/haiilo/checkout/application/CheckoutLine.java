package com.haiilo.checkout.application;

import com.haiilo.checkout.pricing.model.PricingResponse;

import java.math.BigDecimal;
import java.util.Objects;

public record CheckoutLine(
        String productId,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal,
        String currency,
        String availableOfferType,
        String availableOfferDescription,
        boolean offerApplied
) {
    public CheckoutLine {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");
        Objects.requireNonNull(lineTotal, "lineTotal must not be null");
        Objects.requireNonNull(currency, "currency must not be null");

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
                response.currency(),
                response.availableOfferType(),
                response.availableOfferDescription(),
                response.offerApplied()
        );
    }
}