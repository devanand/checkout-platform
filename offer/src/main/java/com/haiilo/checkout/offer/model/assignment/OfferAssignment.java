package com.haiilo.checkout.offer.model.assignment;

import com.haiilo.checkout.offer.model.rule.Offer;

import java.util.Objects;

public record OfferAssignment(String productId, Offer offer) {

    public OfferAssignment {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(offer, "offer must not be null");
    }
}