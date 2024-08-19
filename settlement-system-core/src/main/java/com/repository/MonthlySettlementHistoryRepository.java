package com.repository;

import com.entity.MonthlySettlementHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlySettlementHistoryRepository extends JpaRepository<MonthlySettlementHistory, Long> {
}
