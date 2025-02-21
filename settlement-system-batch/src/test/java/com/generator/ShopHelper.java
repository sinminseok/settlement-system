package com.generator;

import com.domain.order.entity.Order;
import com.domain.shop.entity.Shop;
import com.domain.order.constants.OrderStatus;
import com.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.generator.UserHelper.createUser;

public class ShopHelper {

    public static Shop createShop(User user, String shopName, LocalDateTime startDateTime, LocalDateTime cancelDateTime , LocalDateTime completionDateTime){
        Shop shop = Shop.builder()
                .id(UUID.randomUUID())
                .user(user)
                .name(shopName)
                .build();

        for(int i=0; i < 10; i++) {
            Order transaction = Order.builder()
                    .price(1000 + i*1000)
                    .status(OrderStatus.COMPLEMENT)
                    .startDateTime(startDateTime)
                    .cancelDateTime(cancelDateTime)
                    .completionDateTime(completionDateTime)
                    .build();
            shop.addTransaction(transaction);
        }
        return shop;
    }
}
