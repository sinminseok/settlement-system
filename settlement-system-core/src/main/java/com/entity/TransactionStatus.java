package com.entity;

/**
 * 거래 상태를 나타낼 Enum
 */
public enum TransactionStatus {
    DURING, // 거래 중
    CANCEL, // 환불, 취소
    COMPLEMENT // 완료
}
