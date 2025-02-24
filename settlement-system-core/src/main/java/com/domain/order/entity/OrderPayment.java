package com.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 결제 정보를 나타내는 Entity
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_payment")
public class OrderPayment {

    @Id
    @UuidGenerator
    @Column(name = "order_payment_id", nullable = false, updatable = false)
    private UUID id;

    @Setter
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime startDateTime;

    private LocalDateTime cancelDateTime;

    private LocalDateTime completionDatetime;

}
