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
@Transactional
@AllArgsConstructor
public class CustomerService {
    private CustomerRepository customerRepository;
    private WalletRepository walletRepository;

    public int signUp(CustomerDto customerDto) {
        Customer customer = new Customer();
        Wallet wallet = new Wallet();

        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());

        customer.linkWallet(wallet);

        customerRepository.save(customer);

        Wallet savedWallet = walletRepository.save(wallet);

        return savedWallet.getId();
    }
}
