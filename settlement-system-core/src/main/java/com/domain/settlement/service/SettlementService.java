
package com.domain.settlement.service;

import com.domain.settlement.dto.DailySettlementResponse;
import com.domain.settlement.entity.MonthlySettlement;
import com.domain.settlement.entity.Settlement;
import com.domain.settlement.repository.MonthlySettlementRepository;
import com.domain.settlement.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final MonthlySettlementRepository monthlySettlementRepository;

    public Settlement findByIdAndDate(UUID shopId, LocalDate localDate){
        Optional<Settlement> byShopIdAndSettlementDate = settlementRepository.findByShopIdAndSettlementDate(shopId, localDate);
        return byShopIdAndSettlementDate.get();
    }

    public MonthlySettlement findByIdAndMonth(UUID shopId, LocalDate localDate) {
        Optional<MonthlySettlement> byShopIdAndSettlementMonthly = monthlySettlementRepository.findByShopIdAndSettlementMonthly(shopId, localDate);
        return byShopIdAndSettlementMonthly.get();
    }

    public Map<LocalDate, Double> findMonthlySettlement(UUID shopId, LocalDate date) {
        List<Settlement> settlements = settlementRepository.findByMonth(shopId, date);
        return settlements.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getSettlementDateTime().toLocalDate(),
                        Collectors.summingDouble(Settlement::getNetSales)
                ));
    }
}
