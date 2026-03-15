package com.haiilo.checkout.offer.model.rule;

import com.haiilo.checkout.offer.model.AppliedOfferSummary;
import com.haiilo.checkout.offer.model.contract.OfferResult;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Pricing rule that can calculate the total price for a product quantity.
 */
public interface Offer {

    OfferType type();
    boolean isActive(LocalDate date);
    boolean appliesTo(int quantity);
    AppliedOfferSummary toAppliedOfferSummary();
    OfferResult apply(int quantity, BigDecimal unitPrice, String currency);
}
