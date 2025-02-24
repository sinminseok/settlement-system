package com.job;

import com.config.QueryDslConfig;
import com.domain.order.repository.OrderRepository;
import com.domain.settlement.entity.MonthlySettlement;
import com.domain.settlement.entity.Settlement;
import com.domain.settlement.repository.MonthlySettlementRepository;
import com.domain.settlement.repository.NormalizedTransactionRepository;
import com.domain.settlement.repository.SettlementRepository;
import com.domain.shop.repository.ShopRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static com.generator.SettlementHelper.createSettlement;

@SpringBatchTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MonthlySettlementJobConfig.class, TestBatchLegacyConfig.class, QueryDslConfig.class})
public class MonthlySettlementComponents {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private NormalizedTransactionRepository normalizedTransactionRepository;

    @Autowired
    private MonthlySettlementRepository monthlySettlementRepository;

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @BeforeEach
    public void tearDown() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void 월별_정산_생성_테스트() throws Exception {
        //given
        LocalDateTime localDateTime = LocalDateTime.of(2025, 2, 3, 13, 13);
        for (int i = 0; i < 10; i++) {
            List<Settlement> settlements = createSettlement(UUID.randomUUID(), "SHOPNAME" + i, localDateTime);
            settlements.stream()
                    .forEach(settlement -> {
                        settlementRepository.save(settlement);
                    });
        }

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "2025-02-23T14:30:45.123")
                .toJobParameters();

        //when
        jobLauncherTestUtils.launchJob(jobParameters);

        //then
        List<MonthlySettlement> all = monthlySettlementRepository.findAll();

        Assertions.assertThat(all.size()).isEqualTo(10);
    }

    @Test
    void 월별_정산_계산_테스트() throws Exception {
        //given
        LocalDateTime localDateTime = LocalDateTime.of(2025, 2, 3, 13, 13);
        List<Settlement> settlements = createSettlement(UUID.randomUUID(), "SHOPNAME", localDateTime);
        settlements.stream()
                .forEach(settlement -> {
                    settlementRepository.save(settlement);
                });


        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "2025-02-23T14:30:45.123")
                .toJobParameters();

        //when
        jobLauncherTestUtils.launchJob(jobParameters);

        //then
        List<MonthlySettlement> all = monthlySettlementRepository.findAll();


        Assertions.assertThat(all.get(0).getTotalSales()).isEqualTo(10000);
        Assertions.assertThat(all.get(0).getTotalRefunds()).isEqualTo(1000);
        Assertions.assertThat(all.get(0).getNetSales()).isEqualTo(80000);
    }
}
