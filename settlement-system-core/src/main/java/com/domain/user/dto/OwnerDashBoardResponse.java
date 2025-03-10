package com.domain.user.dto;

import com.domain.order.dto.OrderResponse;
import com.domain.settlement.dto.SettlementResponse;
import com.domain.settlement.entity.Settlement;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class OwnerDashBoardResponse {
    private UUID shopId;
    private List<OrderResponse> recentOrders;
                private SettlementResponse todaySettlement;
    private Integer todayOrderCount;
    private List<Double> weeklySales;
}
