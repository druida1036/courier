package com.skipthedishes.courier.models;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryStatementDto {
    private int courierId;
    private BigDecimal amount;

}
