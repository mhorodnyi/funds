package com.banking.funds.service;

import com.banking.funds.controller.dto.DepositDto;
import com.banking.funds.controller.dto.WithdrawDto;
import com.banking.funds.exception.DailyWithdrawLimitExceededException;
import com.banking.funds.exception.WalletNotFoundException;
import com.banking.funds.repository.TransactionRepository;
import com.banking.funds.repository.WalletRepository;
import com.banking.funds.repository.entities.Transaction;
import com.banking.funds.repository.entities.Wallet;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Service
@AllArgsConstructor
public class FundsService {
    private static final int DAILY_WITHDRAW_LIMIT = 5000;
    private static final int SUSPICIOUS_AMOUNT = 10_000;
    private static final int CENTS_TO_EUR_MULTIPLIER = 100;
    private static final String WITHDRAW = "withdraw";
    private static final String DEPOSIT = "deposit";

    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;

    @Transactional
    public void withdraw(WithdrawDto withdrawDto) {
        Wallet wallet = walletRepository.findById(withdrawDto.getWalletId()).orElseThrow(WalletNotFoundException::new);

        Transaction transaction = createTransaction(WITHDRAW);

        defineSuspicious(transaction, withdrawDto.getAmount());

        dailyWithdrawLimitCheck(wallet.getId(), withdrawDto.getAmount());

        wallet.setAmount(wallet.getAmount() - withdrawDto.getAmount());

        walletRepository.save(wallet);

        fillTransaction(transaction, wallet, withdrawDto.getAmount());

        transactionRepository.save(transaction);
    }

    private void dailyWithdrawLimitCheck(int walletId, long newWithdrawAmount) {
        long withdrawToday = transactionRepository.withdrawCountByWalletId(walletId).orElse(0L);

        if (withdrawToday + newWithdrawAmount > DAILY_WITHDRAW_LIMIT * CENTS_TO_EUR_MULTIPLIER) {
            throw new DailyWithdrawLimitExceededException();
        }
    }

    @Transactional
    public void deposit(DepositDto depositDto) {
        Wallet wallet = walletRepository.findById(depositDto.getWalletId()).orElseThrow(WalletNotFoundException::new);

        Transaction transaction = createTransaction(DEPOSIT);

        defineSuspicious(transaction, depositDto.getAmount());

        wallet.setAmount(wallet.getAmount() + depositDto.getAmount());

        walletRepository.save(wallet);

        fillTransaction(transaction, wallet, depositDto.getAmount());

        transactionRepository.save(transaction);
    }

    private Transaction createTransaction(String type) {
        Transaction transaction = new Transaction();
        transaction.setType(type);

        return transaction;
    }

    private void defineSuspicious(Transaction transaction, long amount) {
        if (isSuspicious(amount)) {
            transaction.setSuspicious(true);
        }
    }

    private boolean isSuspicious(long amount) {
        return amount > SUSPICIOUS_AMOUNT * CENTS_TO_EUR_MULTIPLIER;
    }

    private void fillTransaction(Transaction transaction,  Wallet wallet, long amount) {
        transaction.setAmount(amount);
        transaction.setRest(wallet.getAmount());
        transaction.setWallet(wallet);
        transaction.setTimestamp(Date.from(Instant.now()));
        transaction.setRest(wallet.getAmount());
    }
}
