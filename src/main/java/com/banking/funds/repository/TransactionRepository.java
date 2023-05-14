package com.banking.funds.repository;

import com.banking.funds.repository.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(value = "SELECT SUM(amount) FROM Transaction WHERE date(timestamp) = date(NOW()) AND type = 'withdraw' AND wallet_id = :walletId",
            nativeQuery = true)
    Optional<Long> withdrawCountByWalletId(@Param("walletId") int walletId);
}
