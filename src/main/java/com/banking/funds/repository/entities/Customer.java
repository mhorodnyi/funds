package com.banking.funds.repository.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "is_suspicious", nullable = false)
    private boolean isSuspicious = false;

    @OneToMany(mappedBy = "customer")
    private List<Wallet> wallets = new ArrayList<>();

    @OneToOne(mappedBy = "customer")
    private BlockedCustomers blockedCustomers;

    public void linkWallet(Wallet wallet) {
        wallet.setCustomer(this);
        wallets.add(wallet);
    }
}
