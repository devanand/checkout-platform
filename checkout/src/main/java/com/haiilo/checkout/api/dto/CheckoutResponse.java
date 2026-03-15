package com.haiilo.checkout.api.dto;

import com.haiilo.checkout.application.CheckoutLine;
import com.haiilo.checkout.application.CheckoutResult;

import java.util.List;
import java.util.Objects;

public record CheckoutResponse(
        List<CheckoutItemResponse> items,
        String total,
        String currency
) {
    public static CheckoutResponse fromResult(CheckoutResult result) {
        Objects.requireNonNull(result, "result must not be null");

        List<CheckoutItemResponse> items = result.items()
                .stream()
                .map(CheckoutResponse::toItemResponse)
                .toList();

        return new CheckoutResponse(
                items,
                result.total().toPlainString(),
                result.currency()
        );
    }

    private static CheckoutItemResponse toItemResponse(CheckoutLine line) {

        AppliedOfferResponse offer = null;

        if (line.availableOfferType() != null) {
            offer = new AppliedOfferResponse(
                    line.availableOfferType(),
                    line.availableOfferDescription(),
                    line.offerApplied()
            );
        }

        return new CheckoutItemResponse(
                line.productId(),
                line.quantity(),
                line.unitPrice().toPlainString(),
                line.lineTotal().toPlainString(),
                line.currency(),
                offer
        );
    }
}