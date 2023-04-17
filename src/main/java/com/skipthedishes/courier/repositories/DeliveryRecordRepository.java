package com.skipthedishes.courier.repositories;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import com.skipthedishes.courier.entities.DeliveryRecord;
import com.skipthedishes.courier.entities.DeliveryStatement;
import com.skipthedishes.courier.entities.DeliveryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRecordRepository extends JpaRepository<DeliveryRecord, Integer> {

    @Query("SELECT new com.skipthedishes.courier.entities.DeliveryTransaction(d.externalDeliveryId, SUM(d.amount)) "
        + "FROM DeliveryRecord d WHERE d.courierId = :courierId "
        + "AND d.eventDate BETWEEN :startDateTime AND :endDateTime "
        + "GROUP BY d.courierId, d.externalDeliveryId ")
    List<DeliveryTransaction> findTransactions(@Param("courierId") int courierId,
        @Param("startDateTime") OffsetDateTime startDateTime,
        @Param("endDateTime") OffsetDateTime endDateTime);

    @Query("SELECT new com.skipthedishes.courier.entities.DeliveryStatement(d.courierId, SUM(d.amount)) "
        + "FROM DeliveryRecord d WHERE d.courierId = :courierId "
        + "AND d.eventDate BETWEEN :startDateTime AND :endDateTime "
        + "GROUP BY d.courierId")
    Optional<DeliveryStatement> calculateStatement(@Param("courierId") int courierId,
        @Param("startDateTime") OffsetDateTime startDateTime,
        @Param("endDateTime") OffsetDateTime endDateTime);

}
