package com.domain.shop.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ShopResponse {
    private final Long id;
    private final String name;
}
