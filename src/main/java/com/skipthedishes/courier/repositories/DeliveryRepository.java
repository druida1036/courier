package com.skipthedishes.courier.repositories;

import java.util.Optional;

import com.skipthedishes.courier.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    Optional<Delivery> findByExternalDeliveryId(int id);
}
