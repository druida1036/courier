package com.skipthedishes.courier.models;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BonusModifiedEvent {

    @NotNull
    private int bonusId;
    @NotNull
    private int deliveryId;
    @NotNull
    private Long modifiedTimestamp;
    @NotNull
    private BigDecimal value;

}
