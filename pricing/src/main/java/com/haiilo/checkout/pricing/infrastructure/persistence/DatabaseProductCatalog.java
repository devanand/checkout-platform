package com.haiilo.checkout.pricing.infrastructure.persistence;

import com.haiilo.checkout.pricing.catalog.ProductCatalog;
import com.haiilo.checkout.pricing.infrastructure.persistence.entity.ProductEntity;
import com.haiilo.checkout.pricing.infrastructure.persistence.repository.ProductJpaRepository;
import com.haiilo.checkout.pricing.model.Product;
import com.haiilo.checkout.pricing.model.ProductId;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@Primary
public class DatabaseProductCatalog implements ProductCatalog {

    private final ProductJpaRepository productJpaRepository;

    public DatabaseProductCatalog(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = Objects.requireNonNull(productJpaRepository, "productJpaRepository must not be null");
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        Objects.requireNonNull(productId, "productId must not be null");

        return productJpaRepository
                .findById(productId.value())
                .map(this::toDomain);
    }

    private Product toDomain(ProductEntity entity) {
        return new Product(
                ProductId.of(entity.getId()),
                entity.getPriceAmount()
        );
    }
}
