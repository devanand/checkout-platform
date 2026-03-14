package com.haiilo.checkout.offer.engine;


import com.haiilo.checkout.offer.model.pricing.PricingContext;
import com.haiilo.checkout.offer.model.pricing.PricingResult;

import java.util.List;

public interface OfferEngine {

    List<PricingResult> applyOffers(List<PricingContext> contexts);

}
