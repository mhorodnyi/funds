package com.banking.funds.repository.entities;

import com.banking.funds.exception.NotEnoughMoneyException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Wallet {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Getter
    @Column(nullable = false)
    @Min(0)
    private long amount = 0;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "wallet")
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void linkCustomer(Customer customer) {
        this.customer = customer;
        customer.addWallet(this);
    }

    public void increaseAmount(long addend) {
        amount += addend;
    }

    public void decreaseAmount(long subtrahend) {
        if (subtrahend > amount) {
            throw new NotEnoughMoneyException();
        }

        amount -= subtrahend;
    }

}
