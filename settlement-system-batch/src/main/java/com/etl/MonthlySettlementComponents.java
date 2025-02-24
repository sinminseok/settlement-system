package com.etl;

import com.domain.settlement.entity.MonthlySettlement;
import com.domain.settlement.entity.Settlement;
import com.dto.SettlementAggregation;
import com.parameters.DateParameter;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;

import java.time.LocalDateTime;
import java.util.Map;

public class MonthlySettlementComponents {

    public static JpaPagingItemReader<SettlementAggregation> monthlySettlementReader(EntityManagerFactory entityManagerFactory, DateParameter jobParameter) {
        String query = """
        SELECT new com.dto.SettlementAggregation(
            s.shopId, 
            s.shopName, 
            SUM(s.totalRefunds), 
            SUM(s.totalSales), 
            SUM(s.netSales), 
            MAX(s.settlementDateTime)
        )
        FROM Settlement s
        WHERE FUNCTION('YEAR', s.settlementDateTime) = :year
        AND FUNCTION('MONTH', s.settlementDateTime) = :month
        GROUP BY s.shopId, s.shopName
    """;

        return new JpaPagingItemReaderBuilder<SettlementAggregation>()
                .name("monthlySettlementReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .queryString(query)
                .parameterValues(Map.of(
                        "year", jobParameter.getRequestDate().getYear(),
                        "month", jobParameter.getRequestDate().getMonthValue()
                ))
                .build();
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
