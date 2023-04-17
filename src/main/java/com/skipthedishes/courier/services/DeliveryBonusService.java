package com.skipthedishes.courier.services;

import com.skipthedishes.courier.entities.Delivery;
import com.skipthedishes.courier.entities.DeliveryBonus;
import com.skipthedishes.courier.mappers.DeliveryBonusMapper;
import com.skipthedishes.courier.models.BonusModifiedEvent;
import com.skipthedishes.courier.repositories.DeliveryBonusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryBonusService {
    private final DeliveryBonusRepository deliveryBonusRepository;
    private final DeliveryService deliveryService;
    private final DeliveryBonusMapper deliveryBonusMapper;

    public void save(final BonusModifiedEvent event) {
        final Delivery delivery = deliveryService.findByExternalDeliveryId(event.getDeliveryId());
        final DeliveryBonus deliveryBonus = deliveryBonusRepository
            .findByDeliveryIdAndExternalBonusId(delivery.getId(), event.getBonusId())
            .map(adjustment -> updateDeliveryBonus(event, adjustment))
            .orElseGet(() -> deliveryBonusMapper.map(event, delivery));
        deliveryBonusRepository.save(deliveryBonus);
    }

    private DeliveryBonus updateDeliveryBonus(final BonusModifiedEvent event, final DeliveryBonus bonus) {
        deliveryBonusMapper.update(bonus, event.getModifiedTimestamp(), event.getValue());
        return bonus;
    }
}
