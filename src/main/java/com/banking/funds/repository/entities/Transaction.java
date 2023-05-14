package com.banking.funds.repository.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private long amount;

    @Column(nullable = false)
    private long rest;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(nullable = false)
    private boolean isSuspicious = false;
}
