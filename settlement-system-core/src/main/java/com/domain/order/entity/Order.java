package com.domain.order.entity;

import com.domain.order.constants.DiscountType;
import com.domain.shop.entity.Shop;
import com.domain.order.constants.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

/**
 * 거래 내역 정보를 나타낼 Entity
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double price;

    // 거래 시작 시간
    @CreatedDate
    private LocalDateTime startDateTime;

    //거래 취소 시간
    private LocalDateTime cancelDateTime;

    //거래 종료 시간
    private LocalDateTime completionDateTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    //적용된 할인 이벤트
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    public void updateShop(Shop shop){
        this.shop = shop;
    }
}
