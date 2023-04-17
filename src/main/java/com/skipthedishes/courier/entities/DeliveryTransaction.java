package com.skipthedishes.courier.entities;


import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryTransaction {
    private int externalDeliveryId;
    private BigDecimal amount;
}
