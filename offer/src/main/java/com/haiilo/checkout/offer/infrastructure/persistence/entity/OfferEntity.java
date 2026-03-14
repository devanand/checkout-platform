package com.haiilo.checkout.offer.infrastructure.persistence.entity;

import com.haiilo.checkout.offer.model.rule.OfferType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "offers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private OfferType type;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_until", nullable = false)
    private LocalDate validUntil;

    @Column(name = "required_quantity")
    private Integer requiredQuantity;

    @Column(name = "bundle_price", precision = 19, scale = 2)
    private BigDecimal bundlePrice;

    @Column(name = "percentage")
    private Integer percentage;


    public OfferEntity(
            OfferType type,
            String description,
            LocalDate validFrom,
            LocalDate validUntil,
            Integer requiredQuantity,
            BigDecimal bundlePrice,
            Integer percentage
    ) {
        this.type = type;
        this.description = description;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.requiredQuantity = requiredQuantity;
        this.bundlePrice = bundlePrice;
        this.percentage = percentage;
    }
}
