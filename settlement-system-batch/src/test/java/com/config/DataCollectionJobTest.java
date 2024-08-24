package com.config;

import com.entity.NormalizedTransaction;
import com.entity.Shop;
import com.repository.NormalizedTransactionRepository;
import com.repository.ShopRepository;
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
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static com.helper.ShopHelper.createShop;

@SpringBatchTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes={DataCollectionJobConfig.class, TestBatchLegacyConfig.class})
@EnableJpaRepositories(basePackages = "com.repository")
@EntityScan(basePackages = "com.entity")
public class DataCollectionJobTest {


    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private NormalizedTransactionRepository normalizedTransactionRepository;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;


    @BeforeEach
    public void tearDown() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void 데이터_전처리_통합_테스트() throws Exception {
        //given
        int shopSize = 10;
        LocalDateTime startDateTime = LocalDateTime.of(2024,8,23,21,45);
        LocalDateTime completionDateTime = LocalDateTime.of(2024,8,23,22,45);

        for(int i=1; i<shopSize; i++) {
            Shop shop = createShop(i,"SHOP" + i, startDateTime, null, completionDateTime);
            shopRepository.save(shop);
        }

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "20240823")
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        List<NormalizedTransaction> all = normalizedTransactionRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(90);
    }
}
