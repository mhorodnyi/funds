package com.banking.funds.controller;

import com.banking.funds.controller.dto.CustomerDto;
import com.banking.funds.service.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CustomerController {
    private CustomerService customerService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public int signUpCustomer(@Valid @RequestBody CustomerDto customerDto) {
        return customerService.signUp(customerDto);
    }
}
