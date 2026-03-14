package com.haiilo.checkout.offer.infrastructure.persistence.repository;

import com.haiilo.checkout.offer.infrastructure.persistence.entity.OfferAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferAssignmentJpaRepository extends JpaRepository<OfferAssignmentEntity, Long> {

    List<OfferAssignmentEntity> findByProductIdOrderByPriorityAsc(String productId);
}
