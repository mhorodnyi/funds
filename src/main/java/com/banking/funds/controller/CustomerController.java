package com.banking.funds.controller;

import com.banking.funds.controller.dto.CustomerDto;
import com.banking.funds.service.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CustomerController {
    private CustomerService customerService;

    @PostMapping("/signup")
    public int signUpCustomer(@Valid @RequestBody CustomerDto customerDto) {
        return customerService.signUp(customerDto);
    }
}
