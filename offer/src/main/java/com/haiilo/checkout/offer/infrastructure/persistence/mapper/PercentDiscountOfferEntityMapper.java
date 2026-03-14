package com.haiilo.checkout.offer.infrastructure.persistence.mapper;

import com.haiilo.checkout.offer.infrastructure.persistence.entity.OfferEntity;
import com.haiilo.checkout.offer.model.ValidityPeriod;
import com.haiilo.checkout.offer.model.rule.Offer;
import com.haiilo.checkout.offer.model.rule.OfferType;
import com.haiilo.checkout.offer.model.rule.PercentDiscountOffer;
import org.springframework.stereotype.Component;

@Component
public class PercentDiscountOfferEntityMapper implements OfferEntityMapper {

    @Override
    public OfferType supportedType() {
        return OfferType.PERCENT_DISCOUNT;
    }

    @Override
    public Offer toDomain(OfferEntity entity) {
        ValidityPeriod validityPeriod = new ValidityPeriod(
                entity.getValidFrom(),
                entity.getValidUntil()
        );

        return new PercentDiscountOffer(
                entity.getType(),
                entity.getDescription(),
                validityPeriod,
                entity.getPercentage()
        );
    }
}
