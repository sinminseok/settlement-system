package com.helper;

import com.domain.order.entity.Order;
import com.domain.shop.entity.Shop;
import com.domain.order.constants.OrderStatus;

import java.time.LocalDateTime;

public class ShopHelper {

    public static Shop createShop(int shopId, String shopName,LocalDateTime startDateTime, LocalDateTime cancelDateTime ,LocalDateTime completionDateTime){
        Shop shop = Shop.builder()
                .id(Long.valueOf(shopId))
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
