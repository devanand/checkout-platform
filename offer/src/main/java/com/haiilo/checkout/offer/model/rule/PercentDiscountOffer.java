package com.haiilo.checkout.offer.model.rule;

import com.haiilo.checkout.api.money.Money;
import com.haiilo.checkout.api.offer.OfferResult;
import com.haiilo.checkout.offer.model.ValidityPeriod;

import java.util.Objects;

public final class PercentDiscountOffer extends AbstractOffer {

    private final int percentage;

    public PercentDiscountOffer(
            OfferType type,
            String description,
            ValidityPeriod validityPeriod,
            int percentage) {
        super(type, description, validityPeriod);

        if (percentage <= 0 || percentage >= 100) {
            throw new IllegalArgumentException("percentage must be between 1 and 99");
        }

        this.percentage = percentage;
    }

    @Override
    public boolean appliesTo(int quantity) {
        return quantity > 0;
    }

    @Override
    public OfferResult apply(int quantity, Money unitPrice) {
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");

        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be >= 0");
        }

        Money total = unitPrice.multiply(quantity).applyDiscount(percentage);

        return new OfferResult(
                total,
                type().name(),
                toAppliedOfferSummary().description(),
                true);
    }
}