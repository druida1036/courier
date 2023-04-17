package com.skipthedishes.courier.services;

import java.time.Instant;
import java.time.Period;
import java.util.List;

import com.skipthedishes.courier.entities.DeliveryStatement;
import com.skipthedishes.courier.entities.DeliveryTransaction;
import com.skipthedishes.courier.exceptions.NotFoundException;
import com.skipthedishes.courier.mappers.DeliveryRecordMapper;
import com.skipthedishes.courier.models.DeliveryStatementDto;
import com.skipthedishes.courier.models.DeliveryTransactionDto;
import com.skipthedishes.courier.repositories.DeliveryRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.skipthedishes.courier.utils.DateUtils.toDateTime;

@Service
@RequiredArgsConstructor
public class DeliveryTransactionService {

    private static final String DELIVERY_NOT_FOUND = "Delivery with courierId [%s] not found.";
    private final DeliveryRecordRepository deliveryRecordRepository;
    private final DeliveryRecordMapper deliveryRecordMapper;

    public List<DeliveryTransactionDto> findTransactionsByCourierAndDate(final int courierId, final long start,
        final long end) {
        final List<DeliveryTransaction> transactions = deliveryRecordRepository.findTransactions(courierId,
            toDateTime(start), toDateTime(end));
        return deliveryRecordMapper.map(transactions);
    }

    public DeliveryStatementDto calculateStatement(final int courierId) {
        final Instant endDate = Instant.now();
        final Instant startDate = endDate.minus(Period.ofWeeks(1));
        final DeliveryStatement statement = deliveryRecordRepository.calculateStatement(courierId,
            toDateTime(startDate.toEpochMilli()), toDateTime(endDate.toEpochMilli())).orElseThrow(() -> {
            throw new NotFoundException(String.format(DELIVERY_NOT_FOUND, courierId));
        });
        return deliveryRecordMapper.map(statement);
    }

}
