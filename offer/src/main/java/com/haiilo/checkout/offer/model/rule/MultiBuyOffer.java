package com.haiilo.checkout.offer.model.rule;


import com.haiilo.checkout.offer.model.AppliedOfferSummary;
import com.haiilo.checkout.offer.model.contract.OfferResult;
import com.haiilo.checkout.offer.model.ValidityPeriod;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Offer that applies a bundled price when a required quantity
 * of a product is purchased.
 */
public final class MultiBuyOffer extends AbstractOffer {

    private final int requiredQuantity;
    private final BigDecimal bundlePrice;

    public MultiBuyOffer(
            OfferType type,
            String description,
            ValidityPeriod validityPeriod,
            int requiredQuantity,
            BigDecimal bundlePrice
    ) {
        super(type, description, validityPeriod);

        if (requiredQuantity <= 1) {
            throw new IllegalArgumentException("required quantity must be greater than 1");
        }

        this.bundlePrice = Objects.requireNonNull(bundlePrice, "bundlePrice must not be null");

        if (!(bundlePrice.compareTo(BigDecimal.ZERO) > 0)) {
            throw new IllegalArgumentException("bundlePrice must be greater than zero");
        }

        this.requiredQuantity = requiredQuantity;
    }

    @Override
    public boolean appliesTo(int quantity) {
        return quantity >= requiredQuantity;
    }

    @Override
    public OfferResult apply(int quantity, BigDecimal unitPrice, String currency) {
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");
        Objects.requireNonNull(currency, "currency must not be null");

        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be >= 0");
        }

        int bundleCount = quantity / requiredQuantity;
        int remainder = quantity % requiredQuantity;

        BigDecimal bundledTotal = bundlePrice.multiply(BigDecimal.valueOf(bundleCount));
        BigDecimal remainderTotal = unitPrice.multiply(BigDecimal.valueOf(remainder));
        BigDecimal total = bundledTotal.add(remainderTotal).setScale(2, RoundingMode.HALF_UP);

        AppliedOfferSummary summary = toAppliedOfferSummary();

        return new OfferResult(
                total,
                currency,
                summary.type(),
                summary.description(),
                false
        );
    }
}
