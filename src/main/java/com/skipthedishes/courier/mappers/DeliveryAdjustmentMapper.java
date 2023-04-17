package com.skipthedishes.courier.mappers;

import java.math.BigDecimal;

import com.skipthedishes.courier.entities.Delivery;
import com.skipthedishes.courier.entities.DeliveryAdjustment;
import com.skipthedishes.courier.models.AdjustmentModifiedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DeliveryAdjustmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "event.adjustmentId", target = "externalAdjustmentId")
    @Mapping(source = "event.value", target = "amount")
    @Mapping(target = "modifiedAt",
        expression = "java(com.skipthedishes.courier.utils.DateUtils.toDateTime(event.getModifiedTimestamp()))")
    @Mapping(source = "delivery", target = "delivery")
    DeliveryAdjustment map(AdjustmentModifiedEvent event, Delivery delivery);

    @Mapping(target = "modifiedAt",
        expression = "java(com.skipthedishes.courier.utils.DateUtils.toDateTime(modifiedTimestamp))")
    @Mapping(target = "amount", source = "value")
    void update(@MappingTarget DeliveryAdjustment adjustment, Long modifiedTimestamp, BigDecimal value);

}
