package com.skipthedishes.courier.mappers;

import java.util.List;

import com.skipthedishes.courier.entities.DeliveryStatement;
import com.skipthedishes.courier.entities.DeliveryTransaction;
import com.skipthedishes.courier.models.DeliveryStatementDto;
import com.skipthedishes.courier.models.DeliveryTransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryRecordMapper {

    List<DeliveryTransactionDto> map(List<DeliveryTransaction> deliveryTransactions);

    @Mapping(source = "transaction.externalDeliveryId", target = "deliveryId")
    DeliveryTransactionDto map(DeliveryTransaction transaction);

    DeliveryStatementDto map(DeliveryStatement deliveryStatement);
}
