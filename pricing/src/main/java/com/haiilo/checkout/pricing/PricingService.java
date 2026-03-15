package com.haiilo.checkout.pricing;

import com.haiilo.checkout.offer.engine.OfferEngine;
import com.haiilo.checkout.offer.model.contract.OfferRequest;
import com.haiilo.checkout.offer.model.contract.OfferResult;
import com.haiilo.checkout.pricing.catalog.ProductCatalog;
import com.haiilo.checkout.pricing.exception.UnknownProductException;
import com.haiilo.checkout.pricing.model.PricingRequest;
import com.haiilo.checkout.pricing.model.PricingResponse;
import com.haiilo.checkout.pricing.model.Product;
import com.haiilo.checkout.pricing.model.ProductId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PricingService {

    private final ProductCatalog catalog;
    private final OfferEngine offerEngine;

    public PricingService(ProductCatalog catalog, OfferEngine offerEngine) {
        this.catalog = Objects.requireNonNull(catalog, "catalog must not be null");
        this.offerEngine = Objects.requireNonNull(offerEngine, "offerEngine must not be null");
    }

    public List<PricingResponse> price(List<PricingRequest> requests) {
        Objects.requireNonNull(requests, "requests must not be null");

        List<Product> products = new ArrayList<>();
        List<OfferRequest> offerRequests = new ArrayList<>();

        for (PricingRequest request : requests) {
            Product product = catalog.findById(ProductId.of(request.productId()))
                    .orElseThrow(() -> new UnknownProductException(request.productId()));

            products.add(product);
            offerRequests.add(request.toOfferRequest(product.unitPrice()));
        }

        List<OfferResult> offerResults = offerEngine.applyOffers(offerRequests);
        List<PricingResponse> responses = new ArrayList<>();

        for (int i = 0; i < requests.size(); i++) {
            PricingRequest request = requests.get(i);
            Product product = products.get(i);
            OfferResult offerResult = offerResults.get(i);

            responses.add(PricingResponse.fromOfferResult(
                    request,
                    product.unitPrice(),
                    offerResult
            ));
        }

        return responses;
    }
}