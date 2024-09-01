package com.repository;

import com.entity.MonthlySettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MonthlySettlementRepository extends JpaRepository<MonthlySettlement, Long> {

    @Query("SELECT m FROM MonthlySettlement m WHERE m.shopId = :shopId AND FUNCTION('MONTH', m.settlementDateTime) = :month AND FUNCTION('YEAR', m.settlementDateTime) = :year")
    Optional<MonthlySettlement> findByShopIdAndSettlementMonthly(@Param("shopId") Long shopId, @Param("date") LocalDate date);

}
