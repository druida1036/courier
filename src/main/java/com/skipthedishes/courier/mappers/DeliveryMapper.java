package com.skipthedishes.courier.mappers;

import java.math.BigDecimal;

import com.skipthedishes.courier.entities.Delivery;
import com.skipthedishes.courier.models.DeliveryCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "event.deliveryId", target = "externalDeliveryId")
    @Mapping(source = "event.value", target = "amount")
    @Mapping(target = "createdAt",
        expression = "java(com.skipthedishes.courier.utils.DateUtils.toDateTime(event.getCreatedTimestamp()))")
    Delivery map(DeliveryCreatedEvent event);

    @Mapping(target = "createdAt",
        expression = "java(com.skipthedishes.courier.utils.DateUtils.toDateTime(createdTimestamp))")
    @Mapping(target = "amount", source = "value")
    void update(@MappingTarget Delivery delivery, Long createdTimestamp, BigDecimal value);

}
