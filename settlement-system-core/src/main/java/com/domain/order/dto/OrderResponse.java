package com.domain.order.dto;

import com.domain.order.constants.DiscountType;
import com.domain.order.constants.OrderStatus;
import com.domain.order.entity.Order;
import com.domain.order.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;

    private double price;

    private LocalDateTime startDateTime;

    private LocalDateTime cancelDateTime;

    private LocalDateTime completionDateTime;

    private OrderStatus status;

    private DiscountType discountType;

    private PaymentStatus paymentStatus;

    private LocalDateTime paymentStartDateTime;

    private LocalDateTime paymentCancelDateTime;

    private LocalDateTime paymentCompletionDateTime;

    public static OrderResponse from(final Order order){
        return OrderResponse.builder()
                .id(order.getId())
                .price(order.getPrice())
                .startDateTime(order.getStartDateTime())
                .cancelDateTime(order.getCancelDateTime())
                .completionDateTime(order.getCompletionDateTime())
                .status(order.getStatus())
                .discountType(order.getDiscountType())
                .paymentStatus(order.getOrderPayment().getStatus())
                .paymentStartDateTime(order.getOrderPayment().getStartDateTime())
                .paymentCancelDateTime(order.getOrderPayment().getCancelDateTime())
                .paymentCompletionDateTime(order.getOrderPayment().getCompletionDatetime())
                .build();
    }
}
