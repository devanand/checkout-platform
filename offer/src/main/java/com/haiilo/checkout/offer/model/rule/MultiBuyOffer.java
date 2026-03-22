package com.haiilo.checkout.offer.model.rule;

import com.haiilo.checkout.api.money.Money;
import com.haiilo.checkout.api.offer.OfferResult;
import com.haiilo.checkout.offer.model.ValidityPeriod;

import java.util.Objects;

public final class MultiBuyOffer extends AbstractOffer {

    private final int requiredQuantity;
    private final String bundlePrice;

    public MultiBuyOffer(
            OfferType type,
            String description,
            ValidityPeriod validityPeriod,
            int requiredQuantity,
            String bundlePrice) {
        super(type, description, validityPeriod);

        if (requiredQuantity < 2) {
            throw new IllegalArgumentException("required quantity must be at least 2");
        }

        this.bundlePrice = Objects.requireNonNull(bundlePrice, "bundlePrice must not be null");
        this.requiredQuantity = requiredQuantity;
    }

    @Override
    public boolean appliesTo(int quantity) {
        return quantity >= requiredQuantity;
    }

    @Override
    public OfferResult apply(int quantity, Money unitPrice) {
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");

        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be >= 0");
        }

        Money bundlePriceMoney = Money.of(bundlePrice, unitPrice.currency());

        int bundleCount = quantity / requiredQuantity;
        int remainder = quantity % requiredQuantity;

        Money total = bundlePriceMoney.multiply(bundleCount)
                .add(unitPrice.multiply(remainder));

        return new OfferResult(
                total,
                type().name(),
                toAppliedOfferSummary().description(),
                true);
    }
}
