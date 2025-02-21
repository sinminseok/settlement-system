package com.job;

import com.config.QueryDslConfig;
import com.domain.order.entity.OrderTransaction;
import com.domain.settlement.entity.Settlement;
import com.domain.settlement.repository.NormalizedTransactionRepository;
import com.domain.settlement.repository.SettlementRepository;
import com.domain.shop.repository.ShopRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.generator.NormalizedTransactionHelper.createNormalizedTransactions;

@SpringBatchTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes={SettlementCalculationJobConfig.class, TestBatchLegacyConfig.class, QueryDslConfig.class})
public class SettlementCalculationJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private NormalizedTransactionRepository normalizedTransactionRepository;

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;


    @BeforeEach
    public void tearDown() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void 가게별_통계_기능_통합_테스트() throws Exception {
        //given
        LocalDateTime localDateTime = LocalDateTime.of(2024,10,23,13,13);
        for(int i=0; i<10; i++){
            List<OrderTransaction> normalizedTransactions = createNormalizedTransactions(UUID.randomUUID(), "SHOPNAME" + i, localDateTime);
            normalizedTransactions.stream()
                    .forEach(normalizedTransaction -> {
                        normalizedTransactionRepository.save(normalizedTransaction);
                    });
        }

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "2024-10-23T14:30:45.123")
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        List<Settlement> all = settlementRepository.findAll();

        Assertions.assertThat(all.size()).isEqualTo(10);
        Assertions.assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @Test
    void 할인_적용_테스트() throws Exception {
        //VIP_DISCOUNT == 10퍼센트 할인
        //수수료(1개의 거래당) 1000원 할인
        //given
        LocalDateTime localDateTime = LocalDateTime.of(2024,10,23,13,13);

        List<OrderTransaction> normalizedTransactions = createNormalizedTransactions(UUID.randomUUID(), "SHOPNAME", localDateTime);
        normalizedTransactions.stream()
                .forEach(normalizedTransaction -> normalizedTransactionRepository.save(normalizedTransaction));

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "2024-10-23T14:30:45.123")
                .toJobParameters();

        //when
        jobLauncherTestUtils.launchJob(jobParameters);

        //then
        List<Settlement> all = settlementRepository.findAll();

        Settlement settlement = all.get(0);
        Assertions.assertThat(settlement.getTotalSales()).isEqualTo(100000);
        Assertions.assertThat(settlement.getNetSales()).isEqualTo(80000);
    }
}
