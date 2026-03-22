package com.haiilo.checkout.adapter;

import com.haiilo.checkout.api.offer.OfferRequest;
import com.haiilo.checkout.api.offer.OfferResult;
import com.haiilo.checkout.offer.engine.OfferEngine;
import com.haiilo.checkout.pricing.port.OfferPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class OfferPortAdapter implements OfferPort {

    private final OfferEngine offerEngine;

    public OfferPortAdapter(OfferEngine offerEngine) {
        this.offerEngine = Objects.requireNonNull(offerEngine, "offerEngine must not be null");
    }

    @Override
    public List<OfferResult> applyOffers(List<OfferRequest> requests) {
        return offerEngine.applyOffers(requests);
    }
}
