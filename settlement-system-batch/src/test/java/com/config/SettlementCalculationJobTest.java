package com.config;


import com.entity.NormalizedTransaction;
import com.entity.Settlement;
import com.repository.NormalizedTransactionRepository;
import com.repository.SettlementRepository;
import com.repository.ShopRepository;
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
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static com.helper.NormalizedTransactionHelper.createNormalizedTransactions;

@SpringBatchTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes={SettlementCalculationJobConfig.class, TestBatchLegacyConfig.class})
@EnableJpaRepositories(basePackages = "com.repository")
@EntityScan(basePackages = "com.entity")
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
        for(int i=1; i<10; i++){
            List<NormalizedTransaction> normalizedTransactions = createNormalizedTransactions(Long.valueOf(i), "SHOPNAME" + i, localDateTime);
            normalizedTransactions.stream()
                    .forEach(normalizedTransaction -> {
                        normalizedTransactionRepository.save(normalizedTransaction);
                    });
        }

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "20241023")
                .toJobParameters();

        //when

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        List<Settlement> all = settlementRepository.findAll();

        Assertions.assertThat(all.size()).isEqualTo(9);
        Assertions.assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }
}
