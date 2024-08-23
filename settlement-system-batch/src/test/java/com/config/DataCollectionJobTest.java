package com.config;

import com.repository.ShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    private JobRepositoryTestUtils jobRepositoryTestUtils;


    @BeforeEach
    public void tearDown() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void 데이터_전처리_통합_테스트(){
        System.out.println("DASDAS");
    }
}
