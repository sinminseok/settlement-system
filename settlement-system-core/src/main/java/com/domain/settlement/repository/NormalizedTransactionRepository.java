package com.domain.settlement.repository;

import com.domain.order.entity.OrderTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NormalizedTransactionRepository extends JpaRepository<OrderTransaction, UUID> {
}
