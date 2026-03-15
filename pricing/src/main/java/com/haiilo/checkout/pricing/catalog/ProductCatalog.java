package com.haiilo.checkout.pricing.catalog;

import com.haiilo.checkout.pricing.model.Product;
import com.haiilo.checkout.pricing.model.ProductId;

import java.util.Optional;

/**
 * Provides access to products available for purchase.
 *
 * The catalog acts as the source of truth for product pricing
 * during checkout.
 */
public interface ProductCatalog {
    Optional<Product> findById(ProductId productId);
}
