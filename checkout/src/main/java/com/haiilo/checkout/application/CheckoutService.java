package com.haiilo.checkout.application;

import com.haiilo.checkout.domain.Cart;
import com.haiilo.checkout.pricing.PricingService;
import com.haiilo.checkout.pricing.model.PricingRequest;
import com.haiilo.checkout.pricing.model.PricingResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                .map(cartItem -> cartItem.toPricingRequest(DEFAULT_CURRENCY))
                .toList();

        List<PricingResponse> pricingResponses = pricingService.price(pricingRequests);

        List<CheckoutLine> lines = pricingResponses.stream()
                .map(CheckoutLine::fromPricingResponse)
                .toList();

        BigDecimal total = lines.stream()
                .map(CheckoutLine::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);

        String currency = lines.isEmpty() ? DEFAULT_CURRENCY : lines.getFirst().currency();

        return new CheckoutResult(lines, total, currency);
    }

}