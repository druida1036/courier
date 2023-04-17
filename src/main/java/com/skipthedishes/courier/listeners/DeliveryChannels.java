package com.skipthedishes.courier.listeners;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface DeliveryChannels {

    String ADJUSTMENT_MODIFIED = "adjustment-modified";
    String DELIVERY_CREATED = "delivery-created";
    String BONUS_MODIFIED = "bonus-modified";

    @Input(DELIVERY_CREATED)
    SubscribableChannel deliveryCreated();

    @Input(ADJUSTMENT_MODIFIED)
    SubscribableChannel adjustmentModified();

    @Input(BONUS_MODIFIED)
    SubscribableChannel bonusModified();
}
