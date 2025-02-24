package com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SettlementAggregation {

    private UUID shopId;
    private String shopName;
    private double totalRefunds;
    private double totalSales;
    private double netSales;
    private LocalDateTime settlementDateTime;
}