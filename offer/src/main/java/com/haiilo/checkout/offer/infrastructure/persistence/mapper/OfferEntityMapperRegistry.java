package com.haiilo.checkout.offer.infrastructure.persistence.mapper;

import com.haiilo.checkout.offer.infrastructure.persistence.entity.OfferEntity;
import com.haiilo.checkout.offer.model.rule.Offer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class OfferEntityMapperRegistry {

    private final List<OfferEntityMapper> mappers;

    public OfferEntityMapperRegistry(List<OfferEntityMapper> mappers) {
        this.mappers = List.copyOf(Objects.requireNonNull(mappers, "mappers must not be null"));
    }

    public Offer toDomain(OfferEntity entity) {
        return mappers.stream()
                .filter(mapper -> mapper.supportedType() == entity.getType())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported offer type: " + entity.getType()))
                .toDomain(entity);
    }
}
