package com.haiilo.checkout.offer.engine;

import com.haiilo.checkout.api.money.Money;
import com.haiilo.checkout.api.offer.OfferRequest;
import com.haiilo.checkout.api.offer.OfferResult;
import com.haiilo.checkout.offer.catalog.OfferCatalog;
import com.haiilo.checkout.offer.model.AppliedOfferSummary;
import com.haiilo.checkout.offer.model.rule.Offer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultOfferEngine implements OfferEngine {

    private final OfferCatalog offerCatalog;

    public DefaultOfferEngine(OfferCatalog offerCatalog) {
        this.offerCatalog = offerCatalog;
    }

    @Override
    public List<OfferResult> applyOffers(List<OfferRequest> requests) {
        List<OfferResult> results = new ArrayList<>();

        for (OfferRequest request : requests) {
            Offer offer = offerCatalog.findFirstApplicable(request.productId());

            Money regularTotal = request.unitPrice().multiply(request.quantity());

            if (offer == null || !offer.isActive(LocalDate.now())) {
                results.add(OfferResult.noOffer(regularTotal));
                continue;
            }

            if (!offer.appliesTo(request.quantity())) {
                AppliedOfferSummary summary = offer.toAppliedOfferSummary();
                results.add(new OfferResult(
                        regularTotal,
                        summary.type(),
                        summary.description(),
                        false));
                continue;
            }

            results.add(offer.apply(request.quantity(), request.unitPrice()));
        }

        return results;
    }
}
