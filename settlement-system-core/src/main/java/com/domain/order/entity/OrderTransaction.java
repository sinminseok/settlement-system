package com.domain.order.entity;

import com.domain.order.constants.DiscountType;
import com.domain.order.constants.OrderStatus;
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

    @Id
    @UuidGenerator
    @Column(name = "order_transaction_id", nullable = false, updatable = false)
    private UUID id;

    private String shopName;

    private UUID shopId;

    private double price;

    private LocalDateTime completionDateTime; //거래 종료 시간

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

}
