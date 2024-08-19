package com.repository;

import com.entity.DailyTransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyTransactionHistoryRepository extends JpaRepository<DailyTransactionHistory, Long> {
}
