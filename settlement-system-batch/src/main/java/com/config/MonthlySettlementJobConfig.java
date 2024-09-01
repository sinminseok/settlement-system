package com.config;

import com.entity.MonthlySettlement;
import com.entity.Settlement;
import com.parameters.DateParameter;
import com.repository.MonthlySettlementRepository;
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
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Map;

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
    private final MonthlySettlementRepository monthlySettlementRepository;

    private Long currentShopId = 0L;
    private MonthlySettlement currentSettlement;

    @Bean("monthlySettlementParameter")
    @JobScope
    public DateParameter monthlySettlementParameter() {
        return new DateParameter();
    }

    @Bean
    public Job monthlySettlementJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(monthlySettlementStep())
                .next(finalizeMonthlySettlementStep())
                .build();
    }

    @Bean
    @JobScope
    public Step finalizeMonthlySettlementStep() {
        return new StepBuilder("finalStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    monthlySettlementRepository.save(currentSettlement);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }


    @Bean
    @JobScope
    public Step monthlySettlementStep() {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<Settlement, MonthlySettlement>chunk(100, transactionManager)
                .reader(monthlySettlementReader())
                .processor(monthlySettlementProcessor())
                .writer(monthlySettlementJpaItemWriter())
                .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<Settlement> monthlySettlementReader() {
        String query = "SELECT s FROM Settlement s " +
                "WHERE FUNCTION('MONTH', s.settlementDateTime) = FUNCTION('MONTH', :requestDate) " +
                "AND FUNCTION('YEAR', s.settlementDateTime) = FUNCTION('YEAR', :requestDate)" +
                "ORDER BY s.shopId ASC";

        return new JpaPagingItemReaderBuilder<Settlement>()
                .name("monthlySettlementReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .queryString(query)
                .parameterValues(Map.of("requestDate", jobParameter.getRequestDate()))
                .build();
    }

    @Bean
    public ItemProcessor<Settlement, MonthlySettlement> monthlySettlementProcessor(){
        return settlement->{
            if(isSameShop(settlement)) {
                currentSettlement.updateBySettlement(settlement);
                return null;
            }else{
                MonthlySettlement previousSettlement = currentSettlement;
                updateCurrentSettlement(settlement);
                updateCurrentSettlement(settlement);
                return previousSettlement;
            }
        };
    }

    @Bean
    public JpaItemWriter<MonthlySettlement> monthlySettlementJpaItemWriter() {
        JpaItemWriter<MonthlySettlement> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    private boolean isSameShop(Settlement transaction) {
        return currentShopId.equals(transaction.getShopId());
    }

    private void updateCurrentSettlement(Settlement settlement) {
        currentSettlement = createMonthlySettlement(settlement);
        currentShopId = settlement.getShopId();
    }

    private MonthlySettlement createMonthlySettlement(Settlement settlement){
        return MonthlySettlement.builder()
                .shopId(settlement.getShopId())
                .settlementDateTime(LocalDateTime.now())
                .shopName(settlement.getShopName())
                .totalSales(settlement.getTotalSales())
                .totalRefunds(settlement.getTotalRefunds())
                .netSales(settlement.getNetSales())
                .build();
    }

}
