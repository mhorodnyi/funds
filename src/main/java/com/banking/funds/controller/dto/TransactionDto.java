package com.banking.funds.controller.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@Validated
public class TransactionDto {
    @NotNull
    private String userId;

    @Positive
    @Max(2000 * 100)
    private long amount;

}
