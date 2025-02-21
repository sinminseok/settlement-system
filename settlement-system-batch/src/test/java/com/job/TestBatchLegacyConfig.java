package com.job;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@EnableJpaRepositories(basePackages = "com.domain")
@EntityScan(basePackages = "com.domain")
public class TestBatchLegacyConfig {

}