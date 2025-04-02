package com.etl;

import com.domain.settlement.entity.MonthlySettlement;
import com.domain.settlement.entity.QSettlement;
import com.domain.settlement.entity.Settlement;
import com.dto.SettlementAggregation;
import com.parameters.DateParameter;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reader.QuerydslPagingItemReader;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;

public class MonthlySettlementComponents {

    public static ItemReader<SettlementAggregation> monthlySettlementReader(EntityManagerFactory entityManagerFactory, DateParameter jobParameter) {
        QSettlement s = QSettlement.settlement;

        Function<JPAQueryFactory, JPAQuery<SettlementAggregation>> queryFunction = queryFactory ->
                queryFactory.select(Projections.constructor(SettlementAggregation.class,
                                s.shopId,
                                s.shopName,
                                s.totalRefunds.sum(),
                                s.totalSales.sum(),
                                s.netSales.sum(),
                                s.settlementDateTime.max()
                        ))
                        .from(s)
                        .where(s.settlementDateTime.year().eq(jobParameter.getRequestDate().getYear())
                                .and(s.settlementDateTime.month().eq(jobParameter.getRequestDate().getMonthValue())))
                        .groupBy(s.shopId, s.shopName);

        return new QuerydslPagingItemReader<>(entityManagerFactory, 100, queryFunction);
    }


    public static ItemProcessor<SettlementAggregation, MonthlySettlement> monthlySettlementProcessor() {
        return aggregation -> {
            return MonthlySettlement.builder()
                    .shopId(aggregation.getShopId())
                    .shopName(aggregation.getShopName())
                    .settlementDateTime(LocalDateTime.now())
                    .totalRefunds(aggregation.getTotalRefunds())
                    .totalSales(aggregation.getTotalSales())
                    .netSales(aggregation.getNetSales())
                    .build();
        };
    }

    public static JpaItemWriter<MonthlySettlement> monthlySettlementJpaItemWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<MonthlySettlement> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
