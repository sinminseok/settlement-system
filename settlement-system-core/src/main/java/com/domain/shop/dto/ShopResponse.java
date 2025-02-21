package com.domain.shop.dto;

import com.domain.shop.entity.Shop;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class ShopResponse {
    private final UUID id;
    private final String name;
    private final UUID userId;

    public static ShopResponse from(final Shop shop){
        return ShopResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .userId(shop.getShopUserId())
                .build();
    }
}
