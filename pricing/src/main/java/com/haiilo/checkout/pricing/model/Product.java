package com.haiilo.checkout.pricing.model;

import java.math.BigDecimal;
import java.util.Objects;

public record Product(
        ProductId id,
        BigDecimal unitPrice
) {
    public Product {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");
    }
}
