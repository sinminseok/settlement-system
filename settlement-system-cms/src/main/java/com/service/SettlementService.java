package com.service;

import com.dto.DailySettlementResponse;
import com.entity.Settlement;
import com.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;

    public DailySettlementResponse findDailySettlementByShopId(final Long shopId, final LocalDate localDate) {
        Settlement settlement = settlementRepository.findOneByShopIdAndSettlementDate(shopId, localDate)
                .orElseThrow(() -> new IllegalArgumentException("해당 날짜의 정산 내역이 존재하지 않습니다. shopId: " + shopId + ", date: " + localDate));
        return toDailySettlementResponse(settlement);
    }

    private DailySettlementResponse toDailySettlementResponse(Settlement settlement){
        return DailySettlementResponse.builder()
                .id(settlement.getId())
                .shopName(settlement.getShopName())
                .settlementDateTime(settlement.getSettlementDateTime())
                .totalSales(settlement.getTotalSales())
                .totalRefunds(settlement.getTotalRefunds())
                .netSales(settlement.getNetSales())
                .build();
    }
}
