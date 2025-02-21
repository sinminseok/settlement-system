
package com.domain.settlement.service;

import com.domain.settlement.dto.DailySettlementResponse;
import com.domain.settlement.dto.DashboardResponse;
import com.domain.settlement.entity.MonthlySettlement;
import com.domain.settlement.entity.Settlement;
import com.domain.settlement.repository.MonthlySettlementRepository;
import com.domain.settlement.repository.SettlementRepository;
import com.utils.OptionalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

//    public DashboardResponse getDashboardResponse(final UUID shopId, final LocalDate date) {
//        // 오늘의 Settlement 조회
//        Settlement todaySettlement = OptionalUtil.getOrElseThrow(settlementRepository.findByShopIdAndSettlementDate(shopId, date), "존재하지 않는 정산 정보 입니다.");
//        Settlement yesterdaySettlement = OptionalUtil.getOrElseThrow(settlementRepository.findByShopIdAndSettlementDate(shopId, date.minusDays(1)), "존재하지 않는 전날의 정산 정보 입니다.");
//
//        List<Double> weeklyAmounts = getWeeklyAmounts(shopId, date);
//
//        return toDashboardResponse();
//    }

//    private List<Double> getWeeklyAmounts(UUID shopId, LocalDate date){
//        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
//        LocalDate endOfWeek = startOfWeek.plusDays(6);
//
//        List<Settlement> weeklySettlements = settlementRepository.findByShopIdAndSettlementDateBetween(shopId, startOfWeek, endOfWeek);
//        List<Double> weeklySales = new ArrayList<>();
//
//        for (int i = 0; i < 7; i++) {
//            LocalDate currentDay = startOfWeek.plusDays(i);
//            Optional<Settlement> settlementForDay = weeklySettlements.stream()
//                    .filter(s -> s.getSettlementDateTime().toLocalDate().equals(currentDay))
//                    .findFirst();
//            weeklySales.add(settlementForDay.map(Settlement::getNetSales).orElse(0.0));
//        }
//
//    }

//    private DashboardResponse toDashboardResponse(){
//        return  DashboardResponse.builder()
//                .todaySaleAmount(todaySettlement.getNetSales())
//                .todayOrderAmount(todaySettlement.getOrderCount())
//                .compareOrderAmount()
//                .compareSaleAmount()
//                .weeklyAmount(weeklySales)
//                .recentOrders()
//                .build();
//    }

}
