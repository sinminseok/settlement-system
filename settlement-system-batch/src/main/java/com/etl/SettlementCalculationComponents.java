package com.etl;

import com.domain.order.entity.OrderTransaction;
import com.domain.settlement.entity.Settlement;
import com.parameters.DateParameter;
import com.domain.settlement.repository.SettlementRepository;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

public class SettlementCalculationComponents {

    private static UUID currentShopId = UUID.randomUUID();
    private static Settlement currentSettlement;

    public static JpaPagingItemReader<OrderTransaction> settlementReader(EntityManagerFactory entityManagerFactory, DateParameter dateParameter) {
        String query = "SELECT t FROM OrderTransaction t " +
                "WHERE FUNCTION('DATE', t.completionDateTime) = :requestDate " +
                "ORDER BY t.shopId ASC"; // ShopId 로 정렬
        return new JpaPagingItemReaderBuilder<OrderTransaction>()
                .name("settlementReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .queryString(query)
                .parameterValues(Map.of("requestDate", dateParameter.getRequestDate()))
                .build();
    }

    public static ItemProcessor<OrderTransaction, Settlement> settlementItemProcessor() {
        return transaction -> {
            if (isSameShop(transaction)) {
                currentSettlement.updateSettlement(transaction.toInitSettlement());
                return null;
            } else {
                // 즉, 서로 다른 Shop 이 조회되는 순간 Chunk 에 Settlement 를 쌓는다.
                Settlement previousSettlement = currentSettlement;
                updateCurrentSettlement(transaction);
                return previousSettlement;
            }
        };
    }

    @Transactional
    public static void finalizeSettlement(SettlementRepository settlementRepository) {
        if (currentSettlement != null) {
            settlementRepository.save(currentSettlement);
        }
    }

    public static JpaItemWriter<Settlement> settlementJpaItemWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<Settlement> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    private static boolean isSameShop(OrderTransaction transaction) {
        return currentShopId.equals(transaction.getShopId());
    }

    private static void updateCurrentSettlement(OrderTransaction transaction) {
        currentSettlement = transaction.toInitSettlement();
        currentShopId = transaction.getShopId();
    }

}
