package com.haiilo.checkout.offer.catalog;


import com.haiilo.checkout.offer.model.rule.Offer;

public interface OfferCatalog {
    Offer findFirstApplicable(String productId);
}
