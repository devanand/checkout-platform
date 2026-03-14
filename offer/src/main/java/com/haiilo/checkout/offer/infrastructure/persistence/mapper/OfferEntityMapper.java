package com.haiilo.checkout.offer.infrastructure.persistence.mapper;

import com.haiilo.checkout.offer.infrastructure.persistence.entity.OfferEntity;
import com.haiilo.checkout.offer.model.rule.Offer;
import com.haiilo.checkout.offer.model.rule.OfferType;

public interface OfferEntityMapper {

    OfferType supportedType();

    Offer toDomain(OfferEntity entity);
}
