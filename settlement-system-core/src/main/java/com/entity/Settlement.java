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
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long shopId;
    private String shopName;
    private LocalDateTime settlementDateTime;
    private double totalSales;      // 총 매출
    private double totalRefunds;    // 총 환불 금액
    private double netSales;        // 순 매출 (수수료 및 할인이 반영된 금액)

    public void updateSettlement(Settlement settlement) {
        this.totalSales += settlement.getTotalSales();  // 기존 총 매출에 새로 넘어온 총 매출을 더함
        this.totalRefunds += settlement.getTotalRefunds();  // 기존 총 환불 금액에 새로 넘어온 환불 금액을 더함
        this.netSales += settlement.getNetSales();  // 기존 순 매출에 새로 넘어온 순 매출을 더함
    }
}