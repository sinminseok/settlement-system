package com.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySettlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long shopId;
    private String shopName;
    private LocalDateTime settlementDateTime;
    private double totalSales;      // 총 매출
    private double totalRefunds;    // 총 환불 금액
    private double netSales;        // 순 매출 (수수료 및 할인이 반영된 금액)

    public void updateBySettlement(Settlement settlement) {
        this.totalSales += settlement.getTotalSales();
        this.totalRefunds += settlement.getTotalRefunds();
        this.netSales += settlement.getNetSales();
    }
}
