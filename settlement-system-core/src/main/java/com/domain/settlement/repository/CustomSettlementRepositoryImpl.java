package com.domain.settlement.repository;

import com.domain.settlement.entity.QSettlement;
import com.domain.settlement.entity.Settlement;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomSettlementRepositoryImpl implements CustomSettlementRepository{

    private final JPAQueryFactory query;
    private QSettlement qSettlement;

    @Override
    public List<Settlement> findByMonth(UUID shopId, LocalDate localDate) {
        return query.selectFrom(QSettlement.settlement)
                .where(
                        QSettlement.settlement.shopId.eq(shopId),
                        QSettlement.settlement.settlementDateTime.year().eq(localDate.getYear()),
                        QSettlement.settlement.settlementDateTime.month().eq(localDate.getMonthValue())
                )
                .fetch();
    }

    @Override
    public Optional<Settlement> findByShopIdAndSettlementDate(UUID shopId, LocalDate date) {
        Settlement settlement = query.selectFrom(QSettlement.settlement)
                .where(
                        QSettlement.settlement.shopId.eq(shopId),
                        QSettlement.settlement.settlementDateTime.year().eq(date.getYear()),
                        QSettlement.settlement.settlementDateTime.month().eq(date.getMonthValue()),
                        QSettlement.settlement.settlementDateTime.dayOfMonth().eq(date.getDayOfMonth())
                )
                .fetchOne();

        return Optional.ofNullable(settlement);
    }

    @Override
    public List<Settlement> findWeeklySettlement(UUID shopId, LocalDate date) {
        // date가 속한 주의 월요일과 일요일을 계산
        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = date.with(DayOfWeek.SUNDAY);

        return query.selectFrom(QSettlement.settlement)
                .where(
                        QSettlement.settlement.shopId.eq(shopId),
                        QSettlement.settlement.settlementDateTime.goe(startOfWeek.atStartOfDay()),  // 월요일 00:00:00
                        QSettlement.settlement.settlementDateTime.loe(endOfWeek.atTime(23, 59, 59, 999999))  // 일요일 23:59:59
                )
                .fetch();
    }

    @Override
    public List<Settlement> findByShopIdAndSettlementDateBetween(UUID shopId, LocalDate startDate, LocalDate endDate) {
        return query.selectFrom(QSettlement.settlement)
                .where(
                        QSettlement.settlement.shopId.eq(shopId),
                        QSettlement.settlement.settlementDateTime.goe(startDate.atStartOfDay()),  // 시작 날짜 (00:00:00)
                        QSettlement.settlement.settlementDateTime.loe(endDate.atTime(23, 59, 59, 999999))  // 종료 날짜 (23:59:59)
                )
                .fetch();
    }

}
