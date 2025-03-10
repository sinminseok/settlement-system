package com.domain.order.dto;

import com.domain.order.constants.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFilterRequest {
    private OrderStatus orderStatus;
    private LocalDate startTime;
    private LocalDate endTime;
}
