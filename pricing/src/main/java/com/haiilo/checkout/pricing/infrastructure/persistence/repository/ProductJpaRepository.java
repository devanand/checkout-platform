package com.haiilo.checkout.pricing.infrastructure.persistence.repository;

import com.haiilo.checkout.pricing.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, String> {}
