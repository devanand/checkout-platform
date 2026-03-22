package com.haiilo.checkout.pricing;

import com.haiilo.checkout.api.offer.OfferRequest;
import com.haiilo.checkout.api.offer.OfferResult;
import com.haiilo.checkout.api.pricing.PricingRequest;
import com.haiilo.checkout.api.pricing.PricingResponse;
import com.haiilo.checkout.pricing.catalog.ProductCatalog;
import com.haiilo.checkout.pricing.exception.UnknownProductException;
import com.haiilo.checkout.pricing.model.Product;
import com.haiilo.checkout.pricing.model.ProductId;
import com.haiilo.checkout.pricing.port.OfferPort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PricingService {

    private final ProductCatalog catalog;
    private final OfferPort offerPort;

    public PricingService(ProductCatalog catalog, OfferPort offerPort) {
        this.catalog = Objects.requireNonNull(catalog, "catalog must not be null");
        this.offerPort = Objects.requireNonNull(offerPort, "offerPort must not be null");
    }

    public List<PricingResponse> price(List<PricingRequest> requests) {
        Objects.requireNonNull(requests, "requests must not be null");

        List<Product> products = new ArrayList<>();
        List<OfferRequest> offerRequests = new ArrayList<>();

        for (PricingRequest request : requests) {
            Product product = catalog.findById(ProductId.of(request.productId()))
                    .orElseThrow(() -> new UnknownProductException(request.productId()));

            products.add(product);
            offerRequests.add(new OfferRequest(
                    request.productId(),
                    request.quantity(),
                    product.unitPrice()));
        }

        List<OfferResult> offerResults = offerPort.applyOffers(offerRequests);

        List<PricingResponse> responses = new ArrayList<>(requests.size());
        for (int i = 0; i < requests.size(); i++) {
            responses.add(PricingResponse.of(
                    requests.get(i),
                    products.get(i).unitPrice(),
                    offerResults.get(i)));
        }

        return responses;
    }
}
