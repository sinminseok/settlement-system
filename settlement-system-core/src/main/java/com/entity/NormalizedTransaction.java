package com.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private LocalDateTime completionDateTime; //거래 종료 시간

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    public boolean isRefundTransaction(){
        if(status.equals(TransactionStatus.CANCEL)){
            return true;
        }
        return false;
    }

}
