package com.haiilo.checkout.offer.model.pricing;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public record PricingContext(
        String productId,
        int quantity,
        BigDecimal unitPrice,
        String currency
) {
    public PricingContext {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");
        Objects.requireNonNull(currency, "currency must not be null");

        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be >= 0");
        }

        // Validate ISO currency
        Currency.getInstance(currency);

        // Ensure two-decimal precision
        unitPrice = unitPrice.setScale(2, RoundingMode.HALF_UP);
    }
}
