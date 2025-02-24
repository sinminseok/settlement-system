package com.job;

import com.config.QueryDslConfig;
import com.domain.order.entity.OrderTransaction;
import com.domain.shop.entity.Shop;
import com.domain.settlement.repository.NormalizedTransactionRepository;
import com.domain.shop.repository.ShopRepository;
import com.domain.user.entity.User;
import com.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static com.generator.ShopHelper.createShop;
import static com.generator.UserHelper.createUser;

@SpringBatchTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {DataCollectionJobConfig.class, TestBatchLegacyConfig.class, QueryDslConfig.class})
public class DataCollectionJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NormalizedTransactionRepository normalizedTransactionRepository;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;


    @BeforeEach
    public void tearDown() {
        jobRepositoryTestUtils.removeJobExecutions();
        normalizedTransactionRepository.deleteAll();
        shopRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void 데이터_전처리_테스트() throws Exception {
        //given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 8, 23, 21, 45);
        LocalDateTime completionDateTime = LocalDateTime.of(2024, 8, 23, 22, 45);
        User user = userRepository.save(createUser());

        Shop shop = createShop(user,"SHOP" , startDateTime, null, completionDateTime);
        shopRepository.save(shop);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "2024-08-23T14:30:45.123")
                .toJobParameters();

        //when
        jobLauncherTestUtils.launchJob(jobParameters);

        //then
        List<OrderTransaction> all = normalizedTransactionRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(10);
    }

    @Test
    void 검증을_통과하지_못한_내역은_저장하지_않는다() throws Exception {
        //given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 8, 23, 21, 45);
        User user = userRepository.save(createUser());

        Shop shop = createShop(user,"SHOP" , startDateTime, null, null);
        shopRepository.save(shop);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "2024-08-23T14:30:45.123")
                .toJobParameters();

        //when
        jobLauncherTestUtils.launchJob(jobParameters);

        //then
        List<OrderTransaction> all = normalizedTransactionRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(0);
    }
}
