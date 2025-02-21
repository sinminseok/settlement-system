package com.domain.order.entity;

import com.domain.order.constants.DiscountType;
import com.domain.order.constants.OrderStatus;
import com.domain.settlement.entity.Settlement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 데이터 전처리 후 정보를 나타낼 Entity
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderTransaction {

    private static final int FEE = 1000;
    @Id
    @UuidGenerator
    @Column(name = "order_transaction_id", nullable = false, updatable = false)
    private UUID id;

    private String shopName;

    private UUID shopId;

    private double price;

    private LocalDateTime completionDateTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    public boolean isRefundTransaction(){
        if(status.equals(OrderStatus.CANCEL)){
            return true;
        }
        return false;
    }

    public Settlement toInitSettlement(){
        double totalSales = 0.0;
        double totalRefunds = 0.0;
        double netSales = 0.0;

        if (isRefundTransaction()) {
            totalRefunds = this.getPrice();
        } else {
            totalSales = this.getPrice();
            netSales = applyDiscount(this.getPrice(), this.getDiscountType());
        }

        return Settlement.builder()
                .shopId(this.getShopId())
                .shopName(this.getShopName())
                .totalSales(totalSales)
                .settlementDateTime(this.getCompletionDateTime())
                .totalRefunds(totalRefunds)
                .netSales(netSales)
                .build();
    }

    private static double applyDiscount(double originalPrice, DiscountType discountType) {
        return discountType.applyDiscount(originalPrice) - FEE;
    }

}
