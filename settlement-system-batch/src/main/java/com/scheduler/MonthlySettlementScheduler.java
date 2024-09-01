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
public class MonthlySettlementScheduler {

    private final JobLauncher jobLauncher;
    private final Job monthlySettlementJob;

    @Autowired
    public MonthlySettlementScheduler(JobLauncher jobLauncher, @Qualifier("monthlySettlementJob") Job monthlySettlementJob) {
        this.jobLauncher = jobLauncher;
        this.monthlySettlementJob = monthlySettlementJob;
    }

    @Scheduled(cron = "0 0 3 1 * ?")
    public void runSettlementCalculationJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("requestDate", LocalDateTime.now().toString())
                    .toJobParameters();
            jobLauncher.run(monthlySettlementJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
