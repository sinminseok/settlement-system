package com.domain.settlement.entity;

import jakarta.persistence.*;
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

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @Column(name = "shop_name", nullable = false)
    private String shopName;

    @Column(name = "settlement_date_time", nullable = false)
    private LocalDateTime settlementDateTime;

    @Column(name = "total_sales", nullable = true)
    private double totalSales; // 총 매출

    @Column(name = "total_refunds", nullable = true)
    private double totalRefunds; // 총 환불 금액

    @Column(name = "net_sales", nullable = true)
    private double netSales; // 순 매출 (수수료 및 할인이 반영된 금액)

    public void updateBySettlement(Settlement settlement) {
        this.totalSales += settlement.getTotalSales();
        this.totalRefunds += settlement.getTotalRefunds();
        this.netSales += settlement.getNetSales();
    }
}
