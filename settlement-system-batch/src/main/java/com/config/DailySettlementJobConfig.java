package com.config;

import com.entity.NormalizedTransaction;
import com.entity.Settlement;
import com.parameters.DateParameter;
import com.repository.SettlementRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DailySettlementJobConfig {

    private static final String JOB_NAME = "dailySettlementJob";
    private static final String STEP_NAME = "dailySettlementStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final SettlementRepository settlementRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final DateParameter jobParameter;

    @Bean("dataParameter")
    @JobScope
    public DateParameter dateParameter() {
        return new DateParameter();
    }

    @Bean
    public Job dailySettlementJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start()
                .build();
    }

    @Bean
    @JobScope
    public Step settlementCalculationStep() {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<NormalizedTransaction, Settlement>chunk(100, transactionManager)
                .reader(dailySettlementReader())
                .writer(dailySettlementWriter())
                .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<NormalizedTransaction> dailySettlementReader() {
        String query = "SELECT s FROM Settlement s " +
                "WHERE FUNCTION('DATE', s.settlementDateTime) = :requestDate ";
        return new JpaPagingItemReaderBuilder<NormalizedTransaction>()
                .name("dailySettlementReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .queryString(query)
                .parameterValues(Map.of("requestDate", jobParameter.getRequestDate()))
                .build();
    }

}
