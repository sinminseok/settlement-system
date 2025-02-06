package com.generator;

import com.domain.shop.entity.Shop;
import com.domain.user.entity.User;

public class ShopGenerator {

    public static Shop generateShop(User user){
        return Shop.builder()
                .name("TestShop")
                .user(user)
                .build();
    }
}
