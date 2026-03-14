package com.haiilo.checkout.offer.model.rule;

import com.haiilo.checkout.offer.model.AppliedOfferSummary;
import com.haiilo.checkout.offer.model.ValidityPeriod;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Base implementation of the Offer interface providing common metadata.
 */
public abstract class AbstractOffer implements Offer {

    private final OfferType type;
    private final String description;
    private final ValidityPeriod validityPeriod;

    protected AbstractOffer(OfferType type, String description, ValidityPeriod validityPeriod) {
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
        this.validityPeriod = Objects.requireNonNull(validityPeriod, "validityPeriod must not be null");
    }

    @Override
    public OfferType type() {
        return type;
    }

    @Override
    public boolean isActive(LocalDate date) {
        return validityPeriod.isActive(date);
    }

    @Override
    public AppliedOfferSummary toAppliedOfferSummary() {
        return new AppliedOfferSummary(type.name(), description);
    }
}
