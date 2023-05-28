package com.banking.funds.service;

import com.banking.funds.controller.dto.CustomerDto;
import com.banking.funds.repository.entities.Customer;
import org.mapstruct.Mapper;

@Mapper
interface CustomerMapper {
    Customer customerDtoToEntity(CustomerDto customerDto);
}
