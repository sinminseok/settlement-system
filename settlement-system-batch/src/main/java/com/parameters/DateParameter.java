package com.parameters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Getter
@NoArgsConstructor
public class DateParameter {

    private LocalDate requestDate;

    @Value("#{jobParameters[requestDate]}")
    public void setRequestDate(String requestDateString) {
        // `requestDateString`을 LocalDateTime으로 파싱한 후 LocalDate로 변환
        LocalDateTime localDateTime = LocalDateTime.parse(requestDateString, DateTimeFormatter.ISO_DATE_TIME);
        this.requestDate = localDateTime.toLocalDate();
    }
}
