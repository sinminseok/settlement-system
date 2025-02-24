package com.domain.settlement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 일별 정산 내역을 나타낼 entity
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "settlement")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
@AllArgsConstructor
public class Settlement {

    @Id
    @UuidGenerator
    @Column(name = "settlement_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "shop_id", nullable = false)
    private UUID shopId;

    @Column(name = "shop_name", nullable = false)
    private String shopName;

    @Column(name = "settlement_date_time", nullable = false)
    private LocalDateTime settlementDateTime; // 정산 날짜

    @Column(name = "total_sales", nullable = true)
    private double totalSales;      // 총 매출

    @Column(name = "total_refunds", nullable = true)
    private double totalRefunds;    // 총 환불 금액

    @Column(name = "net_sales", nullable = true)
    private double netSales;        // 순 매출 (수수료 및 할인이 반영된 금액)

    @Column(name = "order_count", nullable = true)
    private int orderCount;
}