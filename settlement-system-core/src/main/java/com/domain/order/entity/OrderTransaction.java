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

    @Transient
    private boolean isRefundTransaction;

    public boolean getIsRefundTransaction() {
        return status.equals(OrderStatus.CANCEL);
    }

    public static double applyDiscount(double originalPrice, DiscountType discountType) {
        return discountType.applyDiscount(originalPrice) - FEE;
    }
}
