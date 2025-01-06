package com.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class DailySettlementResponse {
    private final Long id;
    private final String shopName;
    private LocalDateTime settlementDateTime;
    private double totalSales;      // 총 매출
    private double totalRefunds;    // 총 환불 금액
    private double netSales;        // 순 매출 (수수료 및 할인이 반영된 금액)
}
