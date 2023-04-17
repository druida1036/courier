package com.skipthedishes.courier.listeners;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skipthedishes.courier.models.AdjustmentModifiedEvent;
import com.skipthedishes.courier.models.BonusModifiedEvent;
import com.skipthedishes.courier.models.DeliveryCreatedEvent;
import com.skipthedishes.courier.services.DeliveryAdjustmentService;
import com.skipthedishes.courier.services.DeliveryBonusService;
import com.skipthedishes.courier.services.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding({DeliveryChannels.class})
@RequiredArgsConstructor
@Slf4j
public class DeliveryEventListener {

    private final DeliveryService deliveryService;
    private final DeliveryAdjustmentService deliveryAdjustmentService;
    private final DeliveryBonusService deliveryBonusService;
    private final ObjectMapper objectMapper;


    @StreamListener(target = DeliveryChannels.DELIVERY_CREATED)
    @SneakyThrows
    public void deliveryCreatedEventHandler(@Valid final DeliveryCreatedEvent event) {
        log.info(" received delivery created [{}]", objectMapper.writeValueAsString(event));
        deliveryService.save(event);
    }

    @StreamListener(target = DeliveryChannels.ADJUSTMENT_MODIFIED)
    @SneakyThrows
    public void adjustmentModifiedEventHandler(@Valid final AdjustmentModifiedEvent event) {
        log.info(" received adjustment modified [{}]", objectMapper.writeValueAsString(event));
        deliveryAdjustmentService.save(event);
    }

    @StreamListener(target = DeliveryChannels.BONUS_MODIFIED)
    @SneakyThrows
    public void bonusModifiedEventHandler(@Valid final BonusModifiedEvent event) {
        log.info(" received bonus modified [{}]", objectMapper.writeValueAsString(event));
        deliveryBonusService.save(event);
    }
}