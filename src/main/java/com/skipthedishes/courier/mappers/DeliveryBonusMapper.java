package com.skipthedishes.courier.mappers;

import java.math.BigDecimal;

import com.skipthedishes.courier.entities.Delivery;
import com.skipthedishes.courier.entities.DeliveryBonus;
import com.skipthedishes.courier.models.BonusModifiedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DeliveryBonusMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "event.bonusId", target = "externalBonusId")
    @Mapping(source = "event.value", target = "amount")
    @Mapping(target = "modifiedAt",
        expression = "java(com.skipthedishes.courier.utils.DateUtils.toDateTime(event.getModifiedTimestamp()))")
    @Mapping(source = "delivery", target = "delivery")
    DeliveryBonus map(BonusModifiedEvent event, Delivery delivery);

    @Mapping(target = "modifiedAt",
        expression = "java(com.skipthedishes.courier.utils.DateUtils.toDateTime(modifiedTimestamp))")
    @Mapping(target = "amount", source = "value")
    void update(@MappingTarget DeliveryBonus adjustment, Long modifiedTimestamp, BigDecimal value);

}
