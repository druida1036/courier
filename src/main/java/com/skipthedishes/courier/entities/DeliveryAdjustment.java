package com.skipthedishes.courier.entities;


import java.math.BigDecimal;
import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAdjustment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int externalAdjustmentId;
    private OffsetDateTime modifiedAt;
    private BigDecimal amount;
    @ManyToOne(targetEntity = Delivery.class)
    private Delivery delivery;

}
