package com.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class DailySettlementScheduler {


    private final JobLauncher jobLauncher;
    private final Job settlementCalculationJob;

    @Autowired
    public DailySettlementScheduler(JobLauncher jobLauncher, @Qualifier("settlementJob") Job settlementCalculationJob) {
        this.jobLauncher = jobLauncher;
        this.settlementCalculationJob = settlementCalculationJob;
    }


    @Scheduled(cron = "0 0 3 * * ?")
    public void runSettlementCalculationJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("requestDate", LocalDateTime.now().toString())
                    .toJobParameters();
            jobLauncher.run(settlementCalculationJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
