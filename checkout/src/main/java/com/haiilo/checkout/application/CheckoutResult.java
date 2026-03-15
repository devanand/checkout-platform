package com.haiilo.checkout.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public record CheckoutResult(
        List<CheckoutLine> items,
        BigDecimal total,
        String currency
) {
    public CheckoutResult {
        Objects.requireNonNull(items, "items must not be null");
        Objects.requireNonNull(total, "total must not be null");
        Objects.requireNonNull(currency, "currency must not be null");
    }
}