package com.haiilo.checkout.offer.engine;


import com.haiilo.checkout.offer.model.contract.OfferRequest;
import com.haiilo.checkout.offer.model.contract.OfferResult;

import java.util.List;

public interface OfferEngine {

    List<OfferResult> applyOffers(List<OfferRequest> contexts);

}
