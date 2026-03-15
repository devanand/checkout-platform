package com.haiilo.checkout.offer.model.rule;

import com.haiilo.checkout.offer.model.AppliedOfferSummary;
import com.haiilo.checkout.offer.model.contract.OfferResult;
import com.haiilo.checkout.offer.model.ValidityPeriod;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Offer that applies a percentage discount to a product.
 */
public final class PercentDiscountOffer extends AbstractOffer {

    private final int percentage;

    public PercentDiscountOffer(
            OfferType type,
            String description,
            ValidityPeriod validityPeriod,
            int percentage
    ) {
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
    public OfferResult apply(int quantity, BigDecimal unitPrice, String currency) {
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");
        Objects.requireNonNull(currency, "currency must not be null");

        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be >= 0");
        }

        BigDecimal regularTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

        BigDecimal discountMultiplier =
                BigDecimal.valueOf(100 - percentage)
                        .divide(BigDecimal.valueOf(100));

        BigDecimal discountedTotal = regularTotal.multiply(discountMultiplier).setScale(2, RoundingMode.HALF_UP);
        AppliedOfferSummary summary = toAppliedOfferSummary();
        return new OfferResult(
                discountedTotal,
                currency,
                summary.type(),
                summary.description(),
                false
        );
    }
}
