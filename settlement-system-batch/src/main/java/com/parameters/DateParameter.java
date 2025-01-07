package com.parameters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Getter
@NoArgsConstructor
public class DateParameter {

    private LocalDate requestDate;

    @Value("#{jobParameters[requestDate]}")
    public void setRequestDate(LocalDateTime localDateTime) {
        this.requestDate = localDateTime.toLocalDate();
    }

    @Bean
    public FormattingConversionService conversionService() {
        var conversionService = new DefaultFormattingConversionService(true);
        var registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        registrar.registerFormatters(conversionService);
        return conversionService;
    }
}


