package com.parameters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Getter
@NoArgsConstructor
public class DateParameter {

    private LocalDate requestDate;

    @Value("#{jobParameters[requestDate]}")
    public void setRequestDate(String requestDate){
        this.requestDate = LocalDate.parse(requestDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
