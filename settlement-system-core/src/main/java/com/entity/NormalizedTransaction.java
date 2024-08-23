package com.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 여러 가게에서 수집한 거래 정보를 정규화 하는 클래스
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NormalizedTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shopName;

    private Long shopId;

    private double price;

    private TransactionStatus status;

}
