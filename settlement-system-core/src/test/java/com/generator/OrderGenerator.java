package com.generator;

import com.domain.order.constants.DiscountType;
import com.domain.order.constants.OrderStatus;
import com.domain.order.entity.Order;
import com.domain.order.entity.OrderPayment;
import com.domain.order.entity.PaymentStatus;
import com.domain.shop.entity.Shop;

import java.time.LocalDateTime;

public class OrderGenerator {

    public static Order generateOrder(Shop shop){
        // Order 객체 생성
        Order order = Order.builder()
                .shop(shop)
                .price(5000)
                .status(OrderStatus.COMPLEMENT)
                .discountType(DiscountType.NONE)
                .startDateTime(LocalDateTime.now())
                .completionDateTime(LocalDateTime.now())
                .orderPayment(generateOrderPayment())  // OrderPayment 객체 생성
                .build();

        // 생성된 Order 객체를 OrderPayment 객체에 설정
        order.getOrderPayment().setOrder(order);  // 양방향 관계 설정

        return order;
    }

    public static OrderPayment generateOrderPayment(){
        // Order 객체가 설정된 OrderPayment 객체 생성
        return OrderPayment.builder()
                .status(PaymentStatus.COMPLETED)
                .startDateTime(LocalDateTime.now())
                .cancelDateTime(null)
                .completionDatetime(LocalDateTime.now())
                .build();
    }
}
