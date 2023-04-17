package com.skipthedishes.courier.services;

import com.skipthedishes.courier.entities.Delivery;
import com.skipthedishes.courier.exceptions.NotFoundException;
import com.skipthedishes.courier.mappers.DeliveryMapper;
import com.skipthedishes.courier.models.DeliveryCreatedEvent;
import com.skipthedishes.courier.repositories.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private static final String DELIVERY_NOT_FOUND = "Delivery with id [%s] not found.";
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;

    public Delivery findByExternalDeliveryId(final int id) {
        return deliveryRepository.findByExternalDeliveryId(id).orElseThrow(() -> {
            throw new NotFoundException(String.format(DELIVERY_NOT_FOUND, id));
        });
    }

    public void save(final DeliveryCreatedEvent event) {
        final Delivery delivery =
            deliveryRepository.findByExternalDeliveryId(event.getDeliveryId()).map(d -> update(d, event))
                .orElseGet(() -> deliveryMapper.map(event));
        deliveryRepository.save(delivery);
    }

    private Delivery update(Delivery delivery, DeliveryCreatedEvent event) {
        deliveryMapper.update(delivery, event.getCreatedTimestamp(), event.getValue());
        return delivery;
    }
}
