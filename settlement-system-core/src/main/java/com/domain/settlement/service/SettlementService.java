
package com.domain.settlement.service;

import com.domain.settlement.dto.DailySettlementResponse;
import com.domain.settlement.dto.DashboardResponse;
import com.domain.settlement.entity.MonthlySettlement;
import com.domain.settlement.entity.Settlement;
import com.domain.settlement.repository.MonthlySettlementRepository;
import com.domain.settlement.repository.SettlementRepository;
import com.utils.OptionalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final MonthlySettlementRepository monthlySettlementRepository;


    public Settlement findByIdAndDate(final UUID shopId, final LocalDate localDate){
        Optional<Settlement> byShopIdAndSettlementDate = settlementRepository.findByShopIdAndSettlementDate(shopId, localDate);
        return byShopIdAndSettlementDate.get();
    }

    public MonthlySettlement findByIdAndMonth(final UUID shopId, final LocalDate localDate) {
        Optional<MonthlySettlement> byShopIdAndSettlementMonthly = monthlySettlementRepository.findByShopIdAndSettlementMonthly(shopId, localDate);
        return byShopIdAndSettlementMonthly.get();
    }

    /**
     * 날짜의 '달'에 포함된 일별 정산 정보를 모두 조회 (달력에서 사용)
     */
    public Map<LocalDate, Double> findMonthlySettlement(final UUID shopId, final LocalDate date) {
        List<Settlement> settlements = settlementRepository.findByMonth(shopId, date);
        return settlements.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getSettlementDateTime().toLocalDate(),
                        Collectors.summingDouble(Settlement::getNetSales)
                ));
    }


    public List<Double> getWeeklySales(final UUID shopId) {
        List<Settlement> weeklySettlement = settlementRepository.findWeeklySettlement(shopId, LocalDate.now());
        return IntStream.range(0, 7)
                .mapToObj(i -> i < weeklySettlement.size() ? weeklySettlement.get(i).getTotalSales() : 0.0)
                .collect(Collectors.toList());
    }

}
