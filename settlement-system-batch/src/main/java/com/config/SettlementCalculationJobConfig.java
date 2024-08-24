package com.config;

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
 * 정산 계산 로직 Job
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class SettlementCalculationJobConfig {

    private static final String JOB_NAME = "settlementJob";
    private static final String STEP_NAME = "settlementStep";

    private Long currentShopId = 0L;
    private Settlement currentSettlement;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final SettlementRepository settlementRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final DateParameter jobParameter;


    @Bean
    public Job settlementCalculation() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(settlementCalculationStep())
                .next(finalStep())
                .build();
    }

    @Bean
    @JobScope
    public Step settlementCalculationStep() {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<NormalizedTransaction, Settlement>chunk(100, platformTransactionManager)
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
                }, platformTransactionManager)
                .build();
    }

    @StepScope
    public JpaPagingItemReader<NormalizedTransaction> settlementReader() {
        String query = "SELECT t FROM NormalizedTransaction t WHERE FUNCTION('DATE', t.completionDateTime) = :requestDate ORDER BY t.shopId ASC";
        return new JpaPagingItemReaderBuilder<NormalizedTransaction>()
                .name("settlementReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .queryString(query)
                .parameterValues(Map.of("requestDate", jobParameter.getRequestDate()))
                .build();
    }

    public ItemProcessor<NormalizedTransaction, Settlement> settlementItemProcessor() {
        return normalizedTransaction -> {
            if (currentShopId == normalizedTransaction.getShopId()) {
                Settlement updateSettlement = createSettlementByTransaction(normalizedTransaction);
                currentSettlement.updateSettlement(updateSettlement);
                return null;
            } else {
                Settlement previousSettlement = currentSettlement;
                Settlement settlementByTransaction = createSettlementByTransaction(normalizedTransaction);
                currentSettlement = settlementByTransaction;
                currentShopId = normalizedTransaction.getShopId();
                return previousSettlement;
            }
        };
    }

    public JpaItemWriter<Settlement> settlementJpaItemWriter() {
        JpaItemWriter<Settlement> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;

    }

    private Settlement createSettlementByTransaction(NormalizedTransaction normalizedTransaction) {
        return Settlement.builder()
                .shopId(normalizedTransaction.getShopId())
                .shopName(normalizedTransaction.getShopName())
                .totalSales(normalizedTransaction.getPrice())
                .netSales(normalizedTransaction.getPrice() - 10) // 수수료 10 원
                .build();
    }


}
