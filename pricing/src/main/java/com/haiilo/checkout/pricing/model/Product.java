package com.haiilo.checkout.pricing.model;

import com.haiilo.checkout.api.money.Money;

import java.util.Objects;

public record Product(
        ProductId id,
        Money unitPrice) {
    public Product {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");
    }
}
