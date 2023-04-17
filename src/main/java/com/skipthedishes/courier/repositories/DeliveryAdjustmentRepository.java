package com.skipthedishes.courier.repositories;

import java.util.Optional;

import com.skipthedishes.courier.entities.DeliveryAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryAdjustmentRepository extends JpaRepository<DeliveryAdjustment, Integer> {

    Optional<DeliveryAdjustment> findByDeliveryIdAndExternalAdjustmentId(int deliveryId, int adjustmentId);
}
