package com.generator;

import com.domain.settlement.entity.Settlement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SettlementHelper {

    public static List<Settlement> createSettlement(UUID shopId, String shopName, LocalDateTime settlementDateTime){
        List<Settlement> response = new ArrayList<>();

        for(int i=0; i < 10; i++) {
            response.add(Settlement.builder()
                            .shopId(shopId)
                            .shopName(shopName)
                            .settlementDateTime(settlementDateTime)
                            .totalSales(1000)
                            .totalRefunds(100)
                            .netSales(8000)
                    .build());
        }
        return response;
    }
}
