package com.haiilo.checkout.pricing.model;

import java.util.Objects;

/**
 * Value object representing the identifier of a product.
 *
 * Product identifiers are normalized to uppercase and trimmed
 * to avoid inconsistencies during lookup operations.
 */
public record ProductId(String value) {
    public ProductId {
        Objects.requireNonNull(value, "productId must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("productId must not be blank");
        }
        value = value.trim().toUpperCase();
    }

    public static ProductId of(String value) {
        return new ProductId(value);
    }
}
