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

@Service
@AllArgsConstructor
public class FundsService {
    private static final int DAILY_WITHDRAW_LIMIT = 5000;
    private static final int CENTS_TO_EUR_MULTIPLIER = 100;
    private static final String WITHDRAW = "withdraw";
    private static final String DEPOSIT = "deposit";

    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;

    @Transactional
    public void withdraw(WithdrawDto withdrawDto) {
        Wallet wallet = walletRepository.findById(withdrawDto.getWalletId()).orElseThrow(WalletNotFoundException::new);

        dailyWithdrawLimitCheck(wallet.getId(), withdrawDto.getAmount());

        wallet.decreaseAmount(withdrawDto.getAmount());

        walletRepository.save(wallet);

        Transaction transaction = new Transaction(wallet, WITHDRAW);
        transaction.setAmount(withdrawDto.getAmount());

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

        wallet.increaseAmount(depositDto.getAmount());

        walletRepository.save(wallet);

        Transaction transaction = new Transaction(wallet, DEPOSIT);
        transaction.setAmount(depositDto.getAmount());

        transactionRepository.save(transaction);
    }
}
