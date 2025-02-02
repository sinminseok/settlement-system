package com.domain.order.entity;

import com.domain.order.constants.DiscountType;
import com.domain.order.constants.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shopName;

    private Long shopId;

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
