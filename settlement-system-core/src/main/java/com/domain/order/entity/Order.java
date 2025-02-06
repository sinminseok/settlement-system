package com.domain.order.entity;

import com.domain.order.constants.DiscountType;
import com.domain.shop.entity.Shop;
import com.domain.order.constants.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
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

    @Column(name = "price", nullable = false)
    private double price;

    // 거래 시작 시간
    @CreatedDate
    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    //거래 취소 시간
    @Column(name = "cancel_date_time", nullable = true)
    private LocalDateTime cancelDateTime;

    //거래 종료 시간
    @Column(name = "completion_date_time", nullable = true)
    private LocalDateTime completionDateTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private OrderPayment orderPayment;

    public void updateShop(Shop shop){
        this.shop = shop;
    }
}
