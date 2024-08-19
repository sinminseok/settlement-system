package com.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 월별 정산 내역
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySettlementHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long shopId;

    private double revenue;

    // 정산 완료 시간
    private LocalDateTime completionDateTime;
}
