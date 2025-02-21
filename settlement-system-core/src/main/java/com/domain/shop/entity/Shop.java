package com.domain.shop.entity;

import com.domain.common.BaseTimeEntity;
import com.domain.order.entity.Order;
import com.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shop")
@EntityListeners(AuditingEntityListener.class)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Shop extends BaseTimeEntity {

    @Id
    @UuidGenerator
    @Column(name = "shop_id", nullable = false, updatable = false)
    private UUID id;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public UUID getShopUserId(){
        return user.getId();
    }

    public void addTransaction(Order transaction){
        if (this.orders == null) {
            this.orders = new ArrayList<>();
        }
        orders.add(transaction);
        transaction.updateShop(this);
    }
}
