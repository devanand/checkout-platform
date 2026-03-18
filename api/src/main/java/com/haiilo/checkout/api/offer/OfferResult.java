package com.haiilo.checkout.api.offer;

import com.haiilo.checkout.api.money.Money;

import java.util.Objects;

public record OfferResult(
        Money adjustedTotal,
        String offerType,
        String offerDescription,
        boolean offerApplied
) {
    public OfferResult {
        Objects.requireNonNull(adjustedTotal, "adjustedTotal must not be null");
    }

    public static OfferResult noOffer(Money total) {
        return new OfferResult(total, null, null, false);
    }

    public boolean hasOffer() {
        return offerType != null;
    }
}