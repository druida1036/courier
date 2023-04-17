package com.skipthedishes.courier.services;

import com.skipthedishes.courier.entities.Delivery;
import com.skipthedishes.courier.entities.DeliveryAdjustment;
import com.skipthedishes.courier.mappers.DeliveryAdjustmentMapper;
import com.skipthedishes.courier.models.AdjustmentModifiedEvent;
import com.skipthedishes.courier.repositories.DeliveryAdjustmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryAdjustmentService {
    private final DeliveryAdjustmentRepository deliveryAdjustmentRepository;
    private final DeliveryService deliveryService;
    private final DeliveryAdjustmentMapper deliveryAdjustmentMapper;

    public void save(final AdjustmentModifiedEvent event) {
        final Delivery delivery = deliveryService.findByExternalDeliveryId(event.getDeliveryId());
        final DeliveryAdjustment deliveryAdjustment = deliveryAdjustmentRepository
            .findByDeliveryIdAndExternalAdjustmentId(delivery.getId(), event.getAdjustmentId())
            .map(adjustment -> updateDeliveryAdjustment(event, adjustment))
            .orElseGet(() -> deliveryAdjustmentMapper.map(event, delivery));
        deliveryAdjustmentRepository.save(deliveryAdjustment);
    }

    private DeliveryAdjustment updateDeliveryAdjustment(final AdjustmentModifiedEvent event,
        final DeliveryAdjustment adjustment) {
        deliveryAdjustmentMapper.update(adjustment, event.getModifiedTimestamp(), event.getValue());
        return adjustment;
    }
}
