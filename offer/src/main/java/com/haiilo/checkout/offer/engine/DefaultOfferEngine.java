package com.haiilo.checkout.offer.engine;

import com.haiilo.checkout.offer.catalog.OfferCatalog;
import com.haiilo.checkout.offer.model.AppliedOfferSummary;
import com.haiilo.checkout.offer.model.contract.OfferRequest;
import com.haiilo.checkout.offer.model.contract.OfferResult;
import com.haiilo.checkout.offer.model.rule.Offer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

            BigDecimal regularTotal = request.unitPrice().multiply(BigDecimal.valueOf(request.quantity()));

            if (offer == null) {
                results.add(OfferResult.noOffer(regularTotal, request.currency()));
                continue;
            }

            AppliedOfferSummary summary = offer.toAppliedOfferSummary();

            if (!offer.appliesTo(request.quantity())) {
                results.add(new OfferResult(
                        regularTotal,
                        request.currency(),
                        summary.type(),
                        summary.description(),
                        false
                ));
                continue;
            }

            OfferResult appliedResult = offer.apply(
                    request.quantity(),
                    request.unitPrice(),
                    request.currency()
            );

            results.add(new OfferResult(
                    appliedResult.adjustedTotal(),
                    appliedResult.currency(),
                    summary.type(),
                    summary.description(),
                    true
            ));
        }

        return results;
    }
}