package com.domain.settlement.repository;

import com.domain.settlement.entity.Settlement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomSettlementRepository {
    List<Settlement> findByMonth(UUID shopId, LocalDate localDate);

    Optional<Settlement> findByShopIdAndSettlementDate(UUID shopId, LocalDate date);

    List<Settlement> findWeeklySettlement(UUID shopId, LocalDate date);

    List<Settlement> findByShopIdAndSettlementDateBetween(UUID shopId, LocalDate startDate, LocalDate endDate);
}
