package com.banking.funds.service;

import com.banking.funds.controller.dto.DepositDto;
import com.banking.funds.controller.dto.WithdrawDto;
import com.banking.funds.exception.DailyWithdrawLimitExceededException;
import com.banking.funds.exception.WalletNotFoundException;
import com.banking.funds.repository.TransactionRepository;
import com.banking.funds.repository.WalletRepository;
import com.banking.funds.repository.entities.Customer;
import com.banking.funds.repository.entities.Transaction;
import com.banking.funds.repository.entities.Wallet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FundsServiceTest {
    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private FundsService subject;

    @Captor
    private ArgumentCaptor<Wallet> walletArgumentCaptor;

    @Captor
    private ArgumentCaptor<Transaction> transactionArgumentCaptor;

    @Test
    void withdraw_WalletExistsNotSuspiciousLimitNotExceeded_successful() {
        WithdrawDto withdrawDto = WithdrawDto.builder()
                .walletId(1)
                .amount(2000 * 100)
                .build();

        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        Wallet wallet = Wallet.builder()
                .id(1)
                .amount(6000 * 100)
                .customer(customer)
                .build();

        when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
        when(transactionRepository.withdrawCountByWalletId(1)).thenReturn(Optional.of(0L));
        when(walletRepository.save(wallet)).thenReturn(wallet);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(null);

        Assertions.assertDoesNotThrow(() -> subject.withdraw(withdrawDto));
    }

    @Test
    void withdraw_WalletExistsNotSuspiciousLimitNotExceeded_rightOrder() {
        WithdrawDto withdrawDto = WithdrawDto.builder()
                .walletId(1)
                .amount(2000 * 100)
                .build();

        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        Wallet wallet = Wallet.builder()
                .id(1)
                .amount(6000 * 100)
                .customer(customer)
                .build();

        when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
        when(transactionRepository.withdrawCountByWalletId(1)).thenReturn(Optional.of(0L));
        when(walletRepository.save(wallet)).thenReturn(wallet);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(null);

        InOrder order = inOrder(walletRepository, transactionRepository);

        subject.withdraw(withdrawDto);

        order.verify(walletRepository).findById(1);
        order.verify(transactionRepository).withdrawCountByWalletId(1);
        order.verify(walletRepository).save(wallet);
    }

    @Test
    void withdraw_WalletNotExists_WalletNotFoundExceptionThrown() {
        WithdrawDto withdrawDto = WithdrawDto.builder()
                .walletId(1)
                .amount(2000 * 100)
                .build();

        when(walletRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(WalletNotFoundException.class, () -> subject.withdraw(withdrawDto));
    }

    @Test
    void withdraw_WalletExistsLimitExceeded_DailyWithdrawLimitExceededExceptionThrown() {
        WithdrawDto withdrawDto = WithdrawDto.builder()
                .walletId(1)
                .amount(2000 * 100)
                .build();

        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        Wallet wallet = Wallet.builder()
                .id(1)
                .amount(6000 * 100)
                .customer(customer)
                .build();

        when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
        when(transactionRepository.withdrawCountByWalletId(1)).thenReturn(Optional.of(400000L));

        Assertions.assertThrows(DailyWithdrawLimitExceededException.class, () -> subject.withdraw(withdrawDto));
    }

    @Test
    void withdraw_WalletExists_walletAmountReduced() {
        WithdrawDto withdrawDto = WithdrawDto.builder()
                .walletId(1)
                .amount(2000 * 100)
                .build();

        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        Wallet wallet = Wallet.builder()
                .id(1)
                .amount(10000 * 100)
                .customer(customer)
                .build();

        when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
        when(transactionRepository.withdrawCountByWalletId(1)).thenReturn(Optional.of(0L));
        when(walletRepository.save(wallet)).thenReturn(wallet);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(null);

        subject.withdraw(withdrawDto);

        verify(walletRepository).save(walletArgumentCaptor.capture());
        Assertions.assertEquals(walletArgumentCaptor.getValue().getAmount(), 8000 * 100);
    }

    @Test
    void withdraw_transactionCreated_withdrawTransactionType() {
        WithdrawDto withdrawDto = WithdrawDto.builder()
                .walletId(1)
                .amount(2000 * 100)
                .build();

        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        Wallet wallet = Wallet.builder()
                .id(1)
                .amount(10000 * 100)
                .customer(customer)
                .build();

        when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
        when(transactionRepository.withdrawCountByWalletId(1)).thenReturn(Optional.of(0L));
        when(walletRepository.save(wallet)).thenReturn(wallet);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(null);

        subject.withdraw(withdrawDto);

        verify(transactionRepository).save(transactionArgumentCaptor.capture());
        Assertions.assertEquals(transactionArgumentCaptor.getValue().getType(), "withdraw");
    }

    @Test
    void deposit_depositMoreThen10000_transactionSuspicious() {
        DepositDto depositDto = DepositDto.builder()
                .walletId(1)
                .amount(12000 * 100)
                .build();

        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        Wallet wallet = Wallet.builder()
                .id(1)
                .amount(20000 * 100)
                .customer(customer)
                .build();

        when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(null);

        subject.deposit(depositDto);

        verify(transactionRepository).save(transactionArgumentCaptor.capture());
        Assertions.assertTrue(transactionArgumentCaptor.getValue().isSuspicious());
    }

    @Test
    void deposit_depositLessThen10000_transactionSuspicious() {
        DepositDto depositDto = DepositDto.builder()
                .walletId(1)
                .amount(9000 * 100)
                .build();

        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        Wallet wallet = Wallet.builder()
                .id(1)
                .amount(20000 * 100)
                .customer(customer)
                .build();

        when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(null);

        subject.deposit(depositDto);

        verify(transactionRepository).save(transactionArgumentCaptor.capture());
        Assertions.assertFalse(transactionArgumentCaptor.getValue().isSuspicious());
    }

    @Test
    void deposit_transactionCreated_depositTransactionType() {
        DepositDto depositDto = DepositDto.builder()
                .walletId(1)
                .amount(9000 * 100)
                .build();

        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        Wallet wallet = Wallet.builder()
                .id(1)
                .amount(20000 * 100)
                .customer(customer)
                .build();

        when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(null);

        subject.deposit(depositDto);

        verify(transactionRepository).save(transactionArgumentCaptor.capture());
        Assertions.assertEquals(transactionArgumentCaptor.getValue().getType(), "deposit");
    }

    @Test
    void deposit_walletExistsNotSuspicious_rightOrder() {
        DepositDto depositDto = DepositDto.builder()
                .walletId(1)
                .amount(2000 * 100)
                .build();

        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        Wallet wallet = Wallet.builder()
                .id(1)
                .amount(6000 * 100)
                .customer(customer)
                .build();

        when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(null);

        InOrder order = inOrder(walletRepository, transactionRepository);

        subject.deposit(depositDto);

        order.verify(walletRepository).findById(1);
        order.verify(walletRepository).save(wallet);
    }

}
