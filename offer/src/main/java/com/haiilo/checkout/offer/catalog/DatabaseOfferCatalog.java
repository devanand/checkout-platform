package com.haiilo.checkout.offer.catalog;

import com.haiilo.checkout.offer.infrastructure.persistence.entity.OfferAssignmentEntity;
import com.haiilo.checkout.offer.infrastructure.persistence.mapper.OfferEntityMapperRegistry;
import com.haiilo.checkout.offer.infrastructure.persistence.repository.OfferAssignmentJpaRepository;
import com.haiilo.checkout.offer.model.rule.Offer;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Primary
public class DatabaseOfferCatalog implements OfferCatalog {

    private final OfferAssignmentJpaRepository offerAssignmentJpaRepository;
    private final OfferEntityMapperRegistry offerEntityMapperRegistry;

    public DatabaseOfferCatalog(OfferAssignmentJpaRepository offerAssignmentJpaRepository, OfferEntityMapperRegistry offerEntityMapperRegistry) {
        this.offerAssignmentJpaRepository = Objects.requireNonNull(
                offerAssignmentJpaRepository,
                "offerAssignmentJpaRepository must not be null"
        );
        this.offerEntityMapperRegistry = Objects.requireNonNull(
                offerEntityMapperRegistry,
                "offerEntityMapperRegistry must not be null"
        );
    }

    @Override
    public Offer findFirstApplicable(String productId) {
        Objects.requireNonNull(productId, "productId must not be null");

        return offerAssignmentJpaRepository.findByProductIdOrderByPriorityAsc(productId).stream()
                .map(OfferAssignmentEntity::getOffer)
                .map(offerEntityMapperRegistry::toDomain)
                .findFirst().orElse(null);

    }
}
