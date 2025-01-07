package com.job;

import com.entity.NormalizedTransaction;
import com.entity.Settlement;
import com.etl.SettlementCalculationComponents;
import com.parameters.DateParameter;
import com.repository.SettlementRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * 일별 정산 시스템
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class SettlementCalculationJobConfig {

    private static final String JOB_NAME = "settlementJob";
    private static final String STEP_NAME = "settlementStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final SettlementRepository settlementRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final DateParameter jobParameter;


    @Bean(JOB_NAME + "jobParameter")
    @JobScope
    public DateParameter settlementCalculationParameter() {
        return new DateParameter();
    }

    @Bean
    public Job settlementJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(settlementStep())
                .next(finalizeSettlementStep())
                .build();
    }

    @Bean
    @JobScope
    public Step settlementStep() {
        JpaPagingItemReader<NormalizedTransaction> reader = SettlementCalculationComponents.settlementReader(entityManagerFactory, jobParameter);
        ItemProcessor<NormalizedTransaction, Settlement> processor = SettlementCalculationComponents.settlementItemProcessor();
        JpaItemWriter<Settlement> writer = SettlementCalculationComponents.settlementJpaItemWriter(entityManagerFactory);
        return new StepBuilder(STEP_NAME, jobRepository)
                .<NormalizedTransaction, Settlement>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    @JobScope
    public Step finalizeSettlementStep() {
        return new StepBuilder("finalizeSettlementStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    SettlementCalculationComponents.finalizeSettlement(settlementRepository);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

}
