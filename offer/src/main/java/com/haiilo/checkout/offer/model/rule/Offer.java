package com.haiilo.checkout.offer.model.rule;

import com.haiilo.checkout.api.money.Money;
import com.haiilo.checkout.api.offer.OfferResult;
import com.haiilo.checkout.offer.model.AppliedOfferSummary;

import java.time.LocalDate;

public interface Offer {

    OfferType type();

    boolean isActive(LocalDate date);

    boolean appliesTo(int quantity);

    AppliedOfferSummary toAppliedOfferSummary();

    OfferResult apply(int quantity, Money unitPrice);
}
