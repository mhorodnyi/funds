package com.banking.funds.repository.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Getter
    @Setter
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Getter
    @Setter
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Getter
    @Column(name = "is_suspicious", nullable = false)
    private boolean isSuspicious = false;

    @OneToMany(mappedBy = "customer")
    private List<Wallet> wallets = new ArrayList<>();

    @OneToOne(mappedBy = "customer")
    private BlockedCustomers blockedCustomers;

    public void addWallet(Wallet wallet) {
        wallets.add(wallet);
    }
}
