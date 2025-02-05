package com.domain.settlement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 일별 정산 내역을 나타낼 entity
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "settlement")
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settlement_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @Column(name = "shop_name", nullable = false)
    private String shopName;

    @Column(name = "settlement_date_time", nullable = false)
    private LocalDateTime settlementDateTime;

    @Column(name = "total_sales", nullable = true)
    private double totalSales;      // 총 매출

    @Column(name = "total_refunds", nullable = true)
    private double totalRefunds;    // 총 환불 금액

    @Column(name = "net_sales", nullable = true)
    private double netSales;        // 순 매출 (수수료 및 할인이 반영된 금액)

    public void updateSettlement(Settlement settlement) {
        this.totalSales += settlement.getTotalSales();  // 기존 총 매출에 새로 넘어온 총 매출을 더함
        this.totalRefunds += settlement.getTotalRefunds();  // 기존 총 환불 금액에 새로 넘어온 환불 금액을 더함
        this.netSales += settlement.getNetSales();  // 기존 순 매출에 새로 넘어온 순 매출을 더함
    }
}