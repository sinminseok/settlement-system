package com.domain.settlement.dto;

import com.domain.order.dto.OrderResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DashboardResponse {
    private double todaySaleAmount;
    private int todayOrderAmount;
    private double compareSaleAmount;
    private int compareOrderAmount;
    private List<Double> weeklyAmount;
    private List<OrderResponse> recentOrders;
}
