package com.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double price;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    // 거래 시작 시간
    @CreatedDate
    private LocalDateTime startDateTime;

    //거래 취소 시간
    private LocalDateTime cancelDateTime;

    //거래 종료 시간
    private LocalDateTime completionDateTime;
}
