package com.haiilo.checkout.offer.infrastructure.persistence.mapper;

import com.haiilo.checkout.offer.infrastructure.persistence.entity.OfferEntity;
import com.haiilo.checkout.offer.model.ValidityPeriod;
import com.haiilo.checkout.offer.model.rule.MultiBuyOffer;
import com.haiilo.checkout.offer.model.rule.Offer;
import com.haiilo.checkout.offer.model.rule.OfferType;
import org.springframework.stereotype.Component;

@Component
public class MultiBuyOfferEntityMapper implements OfferEntityMapper {

    @Override
    public OfferType supportedType() {
        return OfferType.MULTI_BUY;
    }

    @Override
    public Offer toDomain(OfferEntity entity) {
        ValidityPeriod validityPeriod = new ValidityPeriod(
                entity.getValidFrom(),
                entity.getValidUntil());

        return new MultiBuyOffer(
                entity.getType(),
                entity.getDescription(),
                validityPeriod,
                entity.getRequiredQuantity(),
                entity.getBundlePrice().toPlainString());
    }
}
