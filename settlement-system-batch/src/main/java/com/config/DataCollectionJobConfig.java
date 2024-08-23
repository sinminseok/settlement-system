package com.config;

import com.entity.NormalizedTransaction;
import com.entity.Transaction;
import com.parameters.DateParameter;
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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Map;

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

    @Bean("dataParameter")
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
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Transaction, NormalizedTransaction>chunk(100, platformTransactionManager)
                .reader(dataCollectionReader())
                .processor(dataCollectionProcessor())
                .writer(dataCollectionWriter())
                .build();
    }

    @StepScope
    public JpaPagingItemReader<Transaction> dataCollectionReader() {
        LocalDate requestDate = jobParameter.getRequestDate();
        String query = "SELECT t FROM Transaction t WHERE FUNCTION('DATE', t.completionDateTime) = :requestDate";

        System.out.println("requestDate");
        System.out.println(requestDate);
        return new JpaPagingItemReaderBuilder<Transaction>()
                .name("dataCollectionReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .queryString(query)
                .parameterValues(Map.of("requestDate", requestDate))
                .build();
    }

    public ItemProcessor<Transaction, NormalizedTransaction> dataCollectionProcessor() {
        return transaction -> {
            if (!validateTransaction(transaction)) return null;
            return NormalizedTransaction.builder()
                    .price(transaction.getPrice())
                    .shopId(transaction.getShop().getId())
                    .status(transaction.getStatus())
                    .shopName(transaction.getShop().getName())
                    .build();
        };
    }

    public JpaItemWriter<NormalizedTransaction> dataCollectionWriter() {
        JpaItemWriter<NormalizedTransaction> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    private boolean validateTransaction(Transaction transaction) {
        if (transaction.getCompletionDateTime() != null) {
            return true;
        }
        return false;
    }

}
