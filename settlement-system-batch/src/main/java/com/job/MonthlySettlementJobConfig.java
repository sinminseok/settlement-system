package com.job;

import com.domain.settlement.entity.MonthlySettlement;
import com.dto.SettlementAggregation;
import com.parameters.DateParameter;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import static com.etl.MonthlySettlementComponents.*;

/**
 * 월별 정산 Job
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class MonthlySettlementJobConfig {

    private static final String JOB_NAME = "monthlySettlementJob";
    private static final String STEP_NAME = "monthlySettlementStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final DateParameter jobParameter;

    @Bean("monthlySettlementParameter")
    @JobScope
    public DateParameter monthlySettlementParameter() {
        return new DateParameter();
    }

    @Bean
    public Job monthlySettlementJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(monthlySettlementStep())
                .build();
    }

    @Bean
    @JobScope
    public Step monthlySettlementStep() {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<SettlementAggregation, MonthlySettlement>chunk(100, transactionManager)
                .reader(monthlySettlementReader(entityManagerFactory, jobParameter))
                .processor(monthlySettlementProcessor())
                .writer(monthlySettlementJpaItemWriter(entityManagerFactory))
                .build();
    }
}
