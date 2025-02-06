package com.domain.settlement.repository;

import com.domain.settlement.entity.QSettlement;
import com.domain.settlement.entity.Settlement;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

}
