package com.config;

import com.entity.DiscountType;
import com.entity.NormalizedTransaction;
import com.entity.Settlement;
import com.parameters.DateParameter;
import com.repository.SettlementRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

/**
 * 정
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class SettlementCalculationJobConfig {

    private static final String JOB_NAME = "settlementJob";
    private static final String STEP_NAME = "settlementStep";
    private static final int CHARGE = 1000;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final SettlementRepository settlementRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final DateParameter jobParameter;

    private Long currentShopId = 0L;
    private Settlement currentSettlement;

    @Bean("dataParameter")
    @JobScope
    public DateParameter dateParameter() {
        return new DateParameter();
    }

    @Bean
    public Job settlementCalculationJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(settlementCalculationStep())
                .next(finalStep())
                .build();
    }

    @Bean
    @JobScope
    public Step settlementCalculationStep() {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<NormalizedTransaction, Settlement>chunk(100, transactionManager)
                .reader(settlementReader())
                .processor(settlementItemProcessor())
                .writer(settlementJpaItemWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step finalStep() {
        return new StepBuilder("finalStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    settlementRepository.save(currentSettlement);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<NormalizedTransaction> settlementReader() {
        String query = "SELECT t FROM NormalizedTransaction t " +
                "WHERE FUNCTION('DATE', t.completionDateTime) = :requestDate " +
                "ORDER BY t.shopId ASC";
        return new JpaPagingItemReaderBuilder<NormalizedTransaction>()
                .name("settlementReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .queryString(query)
                .parameterValues(Map.of("requestDate", jobParameter.getRequestDate()))
                .build();
    }

    @Bean
    public ItemProcessor<NormalizedTransaction, Settlement> settlementItemProcessor() {
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

    @Bean
    public JpaItemWriter<Settlement> settlementJpaItemWriter() {
        JpaItemWriter<Settlement> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    private boolean isSameShop(NormalizedTransaction transaction) {
        return currentShopId.equals(transaction.getShopId());
    }

    private void updateCurrentSettlement(NormalizedTransaction transaction) {
        currentSettlement = createSettlement(transaction);
        currentShopId = transaction.getShopId();
    }

    private Settlement createSettlement(NormalizedTransaction transaction) {
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

    private double applyDiscount(double originalPrice, DiscountType discountType) {
        return discountType.applyDiscount(originalPrice) - CHARGE;
    }
}
