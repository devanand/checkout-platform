package com.haiilo.checkout.application;

import com.haiilo.checkout.api.money.Money;

import java.util.List;
import java.util.Objects;

public record CheckoutResult(
        List<CheckoutLine> items,
        Money total) {
    public CheckoutResult {
        Objects.requireNonNull(items, "items must not be null");
        Objects.requireNonNull(total, "total must not be null");
    }
}