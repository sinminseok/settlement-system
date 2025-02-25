package com.v1.settlement;

import com.domain.settlement.dto.SettlementResponse;
import com.domain.settlement.service.SettlementService;
import com.v1.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping("/monthly")
    public ResponseEntity<?> getSettlementMonthly(
            @RequestParam(name = "shopId") UUID shopId,
            @RequestParam(name = "localDate") LocalDate localDate
    ){
        Map<LocalDate, SettlementResponse> monthlySettlement = settlementService.findMonthlySettlement(shopId, localDate);
        SuccessResponse response = new SuccessResponse(true, "기간에 포함된 주문 조회 성공", monthlySettlement);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
