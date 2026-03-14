package com.haiilo.checkout.offer.engine;

import com.haiilo.checkout.offer.catalog.OfferCatalog;
import com.haiilo.checkout.offer.model.pricing.PricingContext;
import com.haiilo.checkout.offer.model.pricing.PricingResult;
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
    public List<PricingResult> applyOffers(List<PricingContext> contexts) {
        List<PricingResult> results = new ArrayList<>();

        for (PricingContext context : contexts) {
            Offer offer = offerCatalog.findFirstApplicable(context.productId());

            if (offer == null) {
                results.add(
                        PricingResult.noOffer(
                                context.unitPrice().multiply(BigDecimal.valueOf(context.quantity())),
                                context.currency()
                        )
                );
                continue;
            }

            results.add(
                    offer.apply(
                            context.quantity(),
                            context.unitPrice(),
                            context.currency()
                    )
            );
        }

        return results;
    }
}