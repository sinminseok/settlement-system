package com.etl;

import com.domain.order.constants.DiscountType;
import com.domain.order.constants.OrderStatus;
import com.domain.order.entity.QOrderTransaction;
import com.domain.settlement.entity.Settlement;
import com.dto.SettlementAggregation;
import com.parameters.DateParameter;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reader.QuerydslPagingItemReader;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public class SettlementCalculationComponents {


    public static ItemReader<SettlementAggregation> settlementReader(EntityManagerFactory entityManagerFactory, DateParameter requestDate) {
        QOrderTransaction t = QOrderTransaction.orderTransaction;

        NumberExpression<Double> cancelPriceSum = new CaseBuilder()
                .when(t.status.eq(OrderStatus.CANCEL)).then(t.price)
                .otherwise(0.0)
                .sum();

        NumberExpression<Double> nonCancelPriceSum = new CaseBuilder()
                .when(t.status.ne(OrderStatus.CANCEL)).then(t.price)
                .otherwise(0.0)
                .sum();

        NumberExpression<Double> discountedPriceSum = new CaseBuilder()
                .when(t.status.ne(OrderStatus.CANCEL))
                .then(new CaseBuilder()
                        .when(t.discountType.eq(DiscountType.VIP_DISCOUNT))
                        .then(t.price.multiply(0.9).subtract(1000))
                        .when(t.discountType.eq(DiscountType.FIRST_ORDER_DISCOUNT))
                        .then(t.price.multiply(0.95).subtract(1000))
                        .otherwise(t.price.subtract(1000)))
                .otherwise(0.0)
                .sum();

        Function<JPAQueryFactory, JPAQuery<SettlementAggregation>> queryFunction = queryFactory ->
                queryFactory.select(Projections.constructor(SettlementAggregation.class,
                                t.shopId,
                                t.shopName,
                                cancelPriceSum,
                                nonCancelPriceSum,
                                discountedPriceSum,
                                t.completionDateTime.max()
                        ))
                        .from(t)
                        .where(t.completionDateTime.year().eq(requestDate.getRequestDate().getYear())
                                .and(t.completionDateTime.month().eq(requestDate.getRequestDate().getMonthValue()))
                                .and(t.completionDateTime.dayOfMonth().eq(requestDate.getRequestDate().getDayOfMonth())))
                        .groupBy(t.shopId);

        return new QuerydslPagingItemReader<>(entityManagerFactory, 100, queryFunction);
    }


    public static ItemProcessor<SettlementAggregation, Settlement> settlementItemProcessor() {
        return aggregation -> {
            return Settlement.builder()
                    .shopId(aggregation.getShopId())
                    .shopName(aggregation.getShopName())
                    .totalSales(aggregation.getTotalSales())
                    .totalRefunds(aggregation.getTotalRefunds())
                    .netSales(aggregation.getNetSales())
                    .settlementDateTime(aggregation.getSettlementDateTime())
                    .build();
        };
    }

    public static JpaItemWriter<Settlement> settlementJpaItemWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<Settlement> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
