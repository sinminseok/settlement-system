package com.controller;

import com.entity.MonthlySettlement;
import com.entity.Settlement;
import com.service.SettlementService;
import com.utils.CsvHelper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping("/daily/download/csv")
    public void downloadDailySettlement(HttpServletResponse response, @RequestParam("shopId") Long shopId, @RequestParam("localDate") LocalDate localDate) throws IOException {
        Settlement settlement = settlementService.findByIdAndDate(shopId, localDate);

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"settlements.csv\"");

        try (Writer writer = response.getWriter()){
            CsvHelper.writeSettlementsToCsv(writer, settlement);
        }
    }

    @GetMapping("/monthly/download/csv")
    public void downloadMonthlySettlement(HttpServletResponse response, @RequestParam("shopId") Long shopId, @RequestParam("localDate") LocalDate localDate) throws IOException {

        response.setContentType("test/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"settlements.csv\"");

        MonthlySettlement monthlySettlement = settlementService.findByIdAndMonth(shopId, localDate);

        try (Writer writer = response.getWriter()){
            CsvHelper.writeMonthlySettlementsToCsv(writer, monthlySettlement);
        }

    }
}
