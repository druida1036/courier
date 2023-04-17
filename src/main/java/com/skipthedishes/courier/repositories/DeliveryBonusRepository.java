package com.skipthedishes.courier.repositories;

import java.util.Optional;

import com.skipthedishes.courier.entities.DeliveryBonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryBonusRepository extends JpaRepository<DeliveryBonus, Integer> {

    Optional<DeliveryBonus> findByDeliveryIdAndExternalBonusId(int deliveryId, int bonusId);
}
