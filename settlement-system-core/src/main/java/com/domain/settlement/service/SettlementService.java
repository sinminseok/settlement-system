package com.domain.settlement.service;

import com.domain.settlement.dto.DailySettlementResponse;
import com.domain.settlement.entity.MonthlySettlement;
import com.domain.settlement.entity.Settlement;
import com.domain.settlement.repository.MonthlySettlementRepository;
import com.domain.settlement.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public DailySettlementResponse findDailySettlementByShopId(final Long shopId, final LocalDate localDate) {
        Settlement settlement = settlementRepository.findOneByShopIdAndSettlementDate(shopId, localDate)
                .orElseThrow(() -> new IllegalArgumentException("해당 날짜의 정산 내역이 존재하지 않습니다. shopId: " + shopId + ", date: " + localDate));
        return toDailySettlementResponse(settlement);
    }

    private DailySettlementResponse toDailySettlementResponse(Settlement settlement){
        return DailySettlementResponse.builder()
                .id(settlement.getId())
                .shopName(settlement.getShopName())
                .settlementDateTime(settlement.getSettlementDateTime())
                .totalSales(settlement.getTotalSales())
                .totalRefunds(settlement.getTotalRefunds())
                .netSales(settlement.getNetSales())
                .build();
    }
}
