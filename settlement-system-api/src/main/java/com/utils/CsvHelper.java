package com.utils;

import com.entity.MonthlySettlement;
import com.entity.Settlement;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;

public class CsvHelper {

    public static void writeSettlementsToCsv(Writer writer, Settlement settlement) {
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                "ShopName", "SettlementDateTime", "TotalSales", "TotalRefunds", "NetSales"))) {

            csvPrinter.printRecord(
                    settlement.getShopName(),
                    settlement.getSettlementDateTime(),
                    settlement.getTotalSales(),
                    settlement.getTotalRefunds(),
                    settlement.getNetSales()
            );

        } catch (IOException e) {
            e.printStackTrace();
            // 로그 처리 또는 예외 처리 로직 추가 가능
        }
    }

    public static void writeMonthlySettlementsToCsv(Writer writer, MonthlySettlement settlement) {
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                "ShopName", "SettlementDateTime", "TotalSales", "TotalRefunds", "NetSales"))) {

            csvPrinter.printRecord(
                    settlement.getShopName(),
                    settlement.getSettlementDateTime(),
                    settlement.getTotalSales(),
                    settlement.getTotalRefunds(),
                    settlement.getNetSales()
            );

        } catch (IOException e) {
            e.printStackTrace();
            // 로그 처리 또는 예외 처리 로직 추가 가능
        }
    }
}
