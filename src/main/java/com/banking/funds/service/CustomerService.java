package com.banking.funds.service;

import com.banking.funds.controller.dto.CustomerDto;
import com.banking.funds.repository.CustomerRepository;
import com.banking.funds.repository.WalletRepository;
import com.banking.funds.repository.entities.Customer;
import com.banking.funds.repository.entities.Wallet;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CustomerService {
    private CustomerRepository customerRepository;
    private WalletRepository walletRepository;
    private CustomerMapper customerMapper;

    @Transactional
    public int signUp(CustomerDto customerDto) {
        Customer customer = customerMapper.customerDtoToEntity(customerDto);
        Wallet wallet = new Wallet();

        wallet.linkCustomer(customer);

        customerRepository.save(customer);

        Wallet savedWallet = walletRepository.save(wallet);

        return savedWallet.getId();
    }
}
