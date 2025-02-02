package com.job;

import com.domain.order.entity.OrderTransaction;
import com.domain.order.entity.Order;
import com.etl.DataCollectionComponents;
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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * 데이터 전처리 Job
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class DataCollectionJobConfig {

    private static final String JOB_NAME = "dataCollectionJob";
    private static final String STEP_NAME = "dataCollectionStep";

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final EntityManagerFactory entityManagerFactory;

    private final DateParameter jobParameter;

    @Bean(JOB_NAME + "jobParameter")
    @JobScope
    public DateParameter dateParameter() {
        return new DateParameter();
    }

    @Bean
    public Job dataCollection() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(dataCollectionStep())
                .build();
    }

    @Bean
    @JobScope
    public Step dataCollectionStep() {
        JpaPagingItemReader<Order> reader = DataCollectionComponents.dataCollectionReader(entityManagerFactory, jobParameter);
        ItemProcessor<Order, OrderTransaction> processor = DataCollectionComponents.dataCollectionProcessor();
        JpaItemWriter<OrderTransaction> writer = DataCollectionComponents.dataCollectionWriter(entityManagerFactory);
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Order, OrderTransaction>chunk(100, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
