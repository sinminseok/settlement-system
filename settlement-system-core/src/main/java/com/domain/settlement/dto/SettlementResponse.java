package com.domain.settlement.dto;

import com.domain.settlement.entity.Settlement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SettlementResponse {

    private UUID id;

    private UUID shopId;

    private String shopName;

    private LocalDateTime settlementDateTime;

    private double totalSales;

    private double totalRefunds;

    private double netSales;

    public static SettlementResponse from(final Settlement settlement){
        return SettlementResponse.builder()
                .id(settlement.getId())
                .shopId(settlement.getShopId())
                .shopName(settlement.getShopName())
                .settlementDateTime(settlement.getSettlementDateTime())
                .totalRefunds(settlement.getTotalRefunds())
                .totalSales(settlement.getTotalSales())
                .netSales(settlement.getNetSales())
                .build();
    }
}
