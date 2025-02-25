
package com.domain.settlement.service;

import com.domain.settlement.dto.DailySettlementResponse;
import com.domain.settlement.dto.DashboardResponse;
import com.domain.settlement.dto.SettlementResponse;
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
import java.time.YearMonth;
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
    public Map<LocalDate, SettlementResponse> findMonthlySettlement(final UUID shopId, final LocalDate date) {
        // 해당 월의 모든 날짜 가져오기
        YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonth());
        List<LocalDate> allDates = IntStream.rangeClosed(1, yearMonth.lengthOfMonth())
                .mapToObj(day -> LocalDate.of(date.getYear(), date.getMonth(), day))
                .collect(Collectors.toList());

        // 해당 월의 정산 데이터 조회
        List<Settlement> settlements = settlementRepository.findByMonth(shopId, date);

        // Settlement 데이터를 LocalDate 기준으로 매핑
        Map<LocalDate, SettlementResponse> settlementMap = settlements.stream()
                .collect(Collectors.toMap(
                        s -> s.getSettlementDateTime().toLocalDate(),
                        SettlementResponse::from
                ));

        // 결과 맵 생성 (데이터가 없는 날짜는 null 값 매핑)
        Map<LocalDate, SettlementResponse> result = new LinkedHashMap<>();
        for (LocalDate day : allDates) {
            result.put(day, settlementMap.getOrDefault(day, null));
        }

        return result;
    }



    public List<Double> getWeeklySales(final UUID shopId) {
        List<Settlement> weeklySettlement = settlementRepository.findWeeklySettlement(shopId, LocalDate.now());
        return IntStream.range(0, 7)
                .mapToObj(i -> i < weeklySettlement.size() ? weeklySettlement.get(i).getTotalSales() : 0.0)
                .collect(Collectors.toList());
    }

}
