package com.haiilo.checkout.api.pricing;

import com.haiilo.checkout.api.money.Money;
import com.haiilo.checkout.api.offer.OfferResult;

import java.util.Objects;

public record PricingResponse(
        String productId,
        int quantity,
        Money unitPrice,
        Money lineTotal,
        String offerType,
        String offerDescription,
        boolean offerApplied) {
    public PricingResponse {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");
        Objects.requireNonNull(lineTotal, "lineTotal must not be null");
    }

    public static PricingResponse of(PricingRequest request, Money unitPrice, OfferResult offerResult) {
        return new PricingResponse(
                request.productId(),
                request.quantity(),
                unitPrice,
                offerResult.adjustedTotal(),
                offerResult.offerType(),
                offerResult.offerDescription(),
                offerResult.offerApplied());
    }
}