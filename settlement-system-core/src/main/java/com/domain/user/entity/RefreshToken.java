package com.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_tokens")
@EntityListeners(AuditingEntityListener.class)
@Getter
public class RefreshToken {

    @Id
    @UuidGenerator
    @Column(name = "refresh_token_id", nullable = false, updatable = false)
    private String id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "token")
    @Setter
    private String token;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    @Builder
    public RefreshToken(User user, String token) {
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }
}