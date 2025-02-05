package com.domain.shop.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class ShopResponse {
    private final UUID id;
    private final String name;
}
