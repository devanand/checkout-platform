package com.haiilo.checkout.offer.model.contract;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public record OfferResult(
        BigDecimal adjustedTotal,
        String currency,
        String offerType,
        String offerDescription,
        boolean offerApplied
) {

    public OfferResult {
        Objects.requireNonNull(adjustedTotal, "adjustedTotal must not be null");
        Objects.requireNonNull(currency, "currency must not be null");

        if (adjustedTotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("adjustedTotal must be >= 0");
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

    public static OfferResult noOffer(BigDecimal total, String currency) {
        return new OfferResult(
                total,
                currency,
                null,
                null,
                false
        );
    }

    public boolean hasOffer() {
        return offerType != null;
    }
}