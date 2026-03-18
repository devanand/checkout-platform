package com.haiilo.checkout.offer.engine;

import com.haiilo.checkout.api.offer.OfferRequest;
import com.haiilo.checkout.api.offer.OfferResult;

import java.util.List;

public interface OfferEngine {

    List<OfferResult> applyOffers(List<OfferRequest> requests);
}