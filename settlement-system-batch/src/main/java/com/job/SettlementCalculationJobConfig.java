package com.job;

import com.domain.settlement.entity.Settlement;
import com.dto.SettlementAggregation;
import com.etl.SettlementCalculationComponents;
import com.parameters.DateParameter;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * 일별 정산 시스템 (하루에 한번 실행)
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class SettlementCalculationJobConfig {

    private static final String JOB_NAME = "settlementJob";
    private static final String STEP_NAME = "settlementStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
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
                .build();
    }

    @Bean
    @JobScope
    public Step settlementStep() {
        JpaPagingItemReader<SettlementAggregation> reader = SettlementCalculationComponents.settlementReader(entityManagerFactory, jobParameter);
        ItemProcessor<SettlementAggregation, Settlement> processor = SettlementCalculationComponents.settlementItemProcessor();
        JpaItemWriter<Settlement> writer = SettlementCalculationComponents.settlementJpaItemWriter(entityManagerFactory);
        return new StepBuilder(STEP_NAME, jobRepository)
                .<SettlementAggregation, Settlement>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
