package com.domain.settlement.repository;

import com.domain.order.entity.OrderTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NormalizedTransactionRepository extends JpaRepository<OrderTransaction, Long> {
}
