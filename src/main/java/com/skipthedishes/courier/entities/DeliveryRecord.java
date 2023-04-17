package com.skipthedishes.courier.entities;


import java.math.BigDecimal;
import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "delivery_records")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRecord {
    @Id
    private int courierId;
    private int externalDeliveryId;
    private OffsetDateTime eventDate;
    private BigDecimal amount;
}
