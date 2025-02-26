package com.domain.order.dto;

import com.domain.order.constants.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPatchRequest {
    private UUID orderId;
    private OrderStatus orderStatus;
}
