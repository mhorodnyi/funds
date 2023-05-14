package com.banking.funds.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
