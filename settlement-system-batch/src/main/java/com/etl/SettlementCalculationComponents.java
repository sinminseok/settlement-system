package com.etl;

import com.domain.order.constants.DiscountType;
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

public class SettlementCalculationComponents {

    private static Long currentShopId = 0L;
    private static Settlement currentSettlement;
    private static final int FEE = 1000;

    public static JpaPagingItemReader<OrderTransaction> settlementReader(EntityManagerFactory entityManagerFactory, DateParameter dateParameter) {
        String query = "SELECT t FROM OrderTransaction t " +
                "WHERE FUNCTION('DATE', t.completionDateTime) = :requestDate " +
                "ORDER BY t.shopId ASC";
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
                currentSettlement.updateSettlement(createSettlement(transaction));
                return null;
            } else {
                Settlement previousSettlement = currentSettlement;
                updateCurrentSettlement(transaction);
                return previousSettlement;
            }
        };
    }

    // 새로운 메서드 추가: finalizing settlement step
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
        currentSettlement = createSettlement(transaction);
        currentShopId = transaction.getShopId();
    }

    private static Settlement createSettlement(OrderTransaction transaction) {
        double totalSales = 0.0;
        double totalRefunds = 0.0;
        double netSales = 0.0;

        if (transaction.isRefundTransaction()) {
            totalRefunds = transaction.getPrice();
        } else {
            totalSales = transaction.getPrice();
            netSales = applyDiscount(transaction.getPrice(), transaction.getDiscountType());
        }

        return Settlement.builder()
                .shopId(transaction.getShopId())
                .shopName(transaction.getShopName())
                .totalSales(totalSales)
                .settlementDateTime(transaction.getCompletionDateTime())
                .totalRefunds(totalRefunds)
                .netSales(netSales)
                .build();
    }

    private static double applyDiscount(double originalPrice, DiscountType discountType) {
        return discountType.applyDiscount(originalPrice) - FEE;
    }
}
