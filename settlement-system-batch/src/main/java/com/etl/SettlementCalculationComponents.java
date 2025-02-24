package com.etl;

import com.domain.settlement.entity.Settlement;
import com.dto.SettlementAggregation;
import com.parameters.DateParameter;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;

import java.util.Map;

public class SettlementCalculationComponents {


    public static JpaPagingItemReader<SettlementAggregation> settlementReader(EntityManagerFactory entityManagerFactory, DateParameter dateParameter) {
        String query = """
            SELECT new com.dto.SettlementAggregation(
                t.shopId,
                t.shopName,
                SUM(CASE WHEN t.status = 'CANCEL' THEN t.price ELSE 0 END),
                SUM(CASE WHEN t.status <> 'CANCEL' THEN t.price ELSE 0 END),
                SUM(CASE WHEN t.status <> 'CANCEL' THEN 
                    CASE 
                        WHEN t.discountType = 'VIP_DISCOUNT' THEN (t.price * 0.9 - 1000)
                        WHEN t.discountType = 'FIRST_ORDER_DISCOUNT' THEN (t.price * 0.95 - 1000)
                        ELSE (t.price - 1000)  
                    END
                ELSE 0 END),
                   MAX(t.completionDateTime)
            )
            FROM OrderTransaction t
            WHERE FUNCTION('DATE', t.completionDateTime) = :requestDate
            GROUP BY t.shopId
        """;

        return new JpaPagingItemReaderBuilder<SettlementAggregation>()
                .name("settlementReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .queryString(query)
                .parameterValues(Map.of("requestDate", dateParameter.getRequestDate()))
                .build();
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
