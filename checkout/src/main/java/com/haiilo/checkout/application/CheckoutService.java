package com.haiilo.checkout.application;

import com.haiilo.checkout.api.money.Money;
import com.haiilo.checkout.api.pricing.PricingRequest;
import com.haiilo.checkout.api.pricing.PricingResponse;
import com.haiilo.checkout.domain.Cart;
import com.haiilo.checkout.pricing.PricingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CheckoutService {

    private static final String DEFAULT_CURRENCY = "EUR";

    private final PricingService pricingService;

    public CheckoutService(PricingService pricingService) {
        this.pricingService = Objects.requireNonNull(pricingService, "pricingService must not be null");
    }

    public CheckoutResult checkout(Cart cart) {
        Objects.requireNonNull(cart, "cart must not be null");

        List<PricingRequest> pricingRequests = cart.items().stream()
                .map(cartItem -> new PricingRequest(
                        cartItem.productId(),
                        cartItem.quantity(),
                        DEFAULT_CURRENCY))
                .toList();

        List<PricingResponse> pricingResponses = pricingService.price(pricingRequests);

        List<CheckoutLine> lines = pricingResponses.stream()
                .map(CheckoutLine::fromPricingResponse)
                .toList();

        Money total = lines.stream()
                .map(CheckoutLine::lineTotal)
                .reduce(Money.zero(DEFAULT_CURRENCY), Money::add);

        return new CheckoutResult(lines, total);
    }
}
