//package com.service;
//
//import com.domain.settlement.entity.MonthlySettlement;
//import com.domain.settlement.entity.Settlement;
//import com.domain.settlement.repository.MonthlySettlementRepository;
//import com.domain.settlement.repository.SettlementRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class SettlementService {
//
//    private final SettlementRepository settlementRepository;
//    private final MonthlySettlementRepository monthlySettlementRepository;
//
//    public Settlement findByIdAndDate(Long shopId, LocalDate localDate){
//        Optional<Settlement> byShopIdAndSettlementDate = settlementRepository.findByShopIdAndSettlementDate(shopId, localDate);
//        return byShopIdAndSettlementDate.get();
//    }
//
//    public MonthlySettlement findByIdAndMonth(Long shopId, LocalDate localDate) {
//        Optional<MonthlySettlement> byShopIdAndSettlementMonthly = monthlySettlementRepository.findByShopIdAndSettlementMonthly(shopId, localDate);
//        return byShopIdAndSettlementMonthly.get();
//    }
//
//}
