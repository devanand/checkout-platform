package com.haiilo.checkout.pricing.model;

import com.haiilo.checkout.offer.model.contract.OfferResult;

import java.math.BigDecimal;
import java.util.Objects;

public record PricingResponse(
        String productId,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal,
        String currency,
        String availableOfferType,
        String availableOfferDescription,
        boolean offerApplied
) {

    public PricingResponse {
        Objects.requireNonNull(productId);
        Objects.requireNonNull(unitPrice);
        Objects.requireNonNull(lineTotal);
        Objects.requireNonNull(currency);
    }

    public static PricingResponse fromOfferResult(
            PricingRequest request,
            BigDecimal unitPrice,
            OfferResult offerResult
    ) {
        return new PricingResponse(
                request.productId(),
                request.quantity(),
                unitPrice,
                offerResult.adjustedTotal(),
                offerResult.currency(),
                offerResult.offerType(),
                offerResult.offerDescription(),
                offerResult.offerApplied()
        );
    }
}