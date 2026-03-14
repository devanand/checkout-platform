package com.haiilo.checkout.offer.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "offer_assignments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OfferAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id", nullable = false)
    private OfferEntity offer;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    public OfferAssignmentEntity(String productId, OfferEntity offer, Integer priority) {
        this.productId = productId;
        this.offer = offer;
        this.priority = priority;
    }
}