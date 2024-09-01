package com.service;

import com.entity.MonthlySettlement;
import com.entity.Settlement;
import com.repository.MonthlySettlementRepository;
import com.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final MonthlySettlementRepository monthlySettlementRepository;

    public Settlement findByIdAndDate(Long shopId, LocalDate localDate){
        Optional<Settlement> byShopIdAndSettlementDate = settlementRepository.findByShopIdAndSettlementDate(shopId, localDate);
        return byShopIdAndSettlementDate.get();
    }

    public MonthlySettlement findByIdAndMonth(Long shopId, LocalDate localDate) {
        Optional<MonthlySettlement> byShopIdAndSettlementMonthly = monthlySettlementRepository.findByShopIdAndSettlementMonthly(shopId, localDate);
        return byShopIdAndSettlementMonthly.get();
    }

}
