package com.domain.settlement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 월별 정산 내역을 나타낼 entity
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySettlement {

    @Id
    @UuidGenerator
    @Column(name = "monthly_settlement_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "shop_id", nullable = false)
    private UUID shopId;

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

}
