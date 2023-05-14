package com.banking.funds.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DepositDto {
    private int walletId;

    @Positive
    @Max(2000 * 100)
    private long amount;
}
