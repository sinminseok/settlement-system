package com.domain.shop.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ShopRequest {
    private String shopName;
    private String ownerEmail;
}
