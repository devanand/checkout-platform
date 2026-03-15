package com.haiilo.checkout.offer.model.contract;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public record OfferRequest(
        String productId,
        int quantity,
        BigDecimal unitPrice,
        String currency
) {

    public OfferRequest {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");
        Objects.requireNonNull(currency, "currency must not be null");

        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }

        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("unitPrice must be >= 0");
        }

        validateCurrency(currency);
    }

    private static void validateCurrency(String currency) {
        try {
            Currency.getInstance(currency);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid currency: " + currency);
        }
    }
}