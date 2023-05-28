package com.banking.funds.repository.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@Entity
@NoArgsConstructor
public class Transaction {
    @Transient
    private static final int SUSPICIOUS_AMOUNT = 10_000;

    @Transient
    private static final int CENTS_TO_EUR_MULTIPLIER = 100;

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Getter
    @Column(nullable = false)
    private String type;

    @Getter
    @Column(nullable = false)
    private long amount;

    @Getter
    @Column(nullable = false)
    private long rest;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Getter
    @Column(nullable = false)
    private boolean isSuspicious = false;

    public Transaction(Wallet wallet, String type) {
        this.type = type;
        this.wallet = wallet;
        this.rest = wallet.getAmount();
        this.timestamp = Date.from(Instant.now());
    }

    public void linkWallet(Wallet wallet) {
        this.wallet = wallet;
        wallet.addTransaction(this);
    }

    public void setAmount(long amount) {
        if (isSuspicious(amount)) {
            isSuspicious = true;
        }

        this.amount = amount;
    }

    private boolean isSuspicious(long amount) {
        return amount > SUSPICIOUS_AMOUNT * CENTS_TO_EUR_MULTIPLIER;
    }
}
