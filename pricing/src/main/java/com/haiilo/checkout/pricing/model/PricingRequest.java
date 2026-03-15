package com.haiilo.checkout.pricing.model;

import com.haiilo.checkout.offer.model.contract.OfferRequest;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public record PricingRequest(
        String productId,
        int quantity,
        String currency
) {

    public PricingRequest {
        Objects.requireNonNull(productId);
        Objects.requireNonNull(currency);

        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }

        validateCurrency(currency);
    }

    private static void validateCurrency(String currency) {
        try {
            Currency.getInstance(currency);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid currency: " + currency);
        }
    }

    public OfferRequest toOfferRequest(BigDecimal unitPrice) {
        return new OfferRequest(
                productId,
                quantity,
                unitPrice,
                currency
        );
    }
}