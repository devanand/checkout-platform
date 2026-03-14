package com.haiilo.checkout.offer.model.pricing;

import com.haiilo.checkout.offer.model.AppliedOfferSummary;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public record PricingResult(
        BigDecimal lineTotal,
        String currency,
        AppliedOfferSummary appliedOffer
) {

    public PricingResult {
        Objects.requireNonNull(lineTotal, "lineTotal must not be null");
        Objects.requireNonNull(currency, "currency must not be null");

        Currency.getInstance(currency);
        lineTotal = lineTotal.setScale(2, RoundingMode.HALF_UP);
    }

    public static PricingResult noOffer(BigDecimal lineTotal, String currency) {
        return new PricingResult(lineTotal, currency, null);
    }
}