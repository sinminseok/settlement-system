package com.repository;

import com.entity.NormalizedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NormalizedTransactionRepository extends JpaRepository<NormalizedTransaction, Long> {
}
