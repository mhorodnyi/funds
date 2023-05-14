package com.banking.funds.repository.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "blocked_customers")
public class BlockedCustomers {
    @Id
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @Temporal(TemporalType.DATE)
    private Date blockedAt;
}
