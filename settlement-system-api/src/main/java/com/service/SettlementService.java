package com.service;

import com.entity.Settlement;
import com.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;

    public Settlement findByIdAndDateTime(Long shopId, LocalDate localDate){
        Optional<Settlement> byShopIdAndSettlementDate = settlementRepository.findByShopIdAndSettlementDate(shopId, localDate);
        return byShopIdAndSettlementDate.get();
    }

}
