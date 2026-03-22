package com.haiilo.checkout.pricing.port;

import com.haiilo.checkout.api.offer.OfferRequest;
import com.haiilo.checkout.api.offer.OfferResult;

import java.util.List;

public interface OfferPort {

    List<OfferResult> applyOffers(List<OfferRequest> requests);
}
