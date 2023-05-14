package com.banking.funds.controller;

import com.banking.funds.controller.dto.WithdrawDto;
import com.banking.funds.controller.dto.DepositDto;
import com.banking.funds.service.FundsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("funds")
@AllArgsConstructor
public class FundsController {

    private FundsService fundsService;

    @PostMapping("withdraw")
    public void withdraw(@RequestBody @Valid WithdrawDto withdrawDto) {
        fundsService.withdraw(withdrawDto);
    }

    @PostMapping("deposit")
    public void deposit(@RequestBody @Valid DepositDto depositDto) {
        fundsService.deposit(depositDto);
    }
}
