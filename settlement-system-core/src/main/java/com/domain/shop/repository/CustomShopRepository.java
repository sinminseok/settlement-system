package com.domain.shop.repository;

import com.domain.shop.entity.Shop;

import java.util.List;

public interface CustomShopRepository {

    List<Shop> findByEmailOrOwnerName(final String email, final String ownerName);

    Shop findByUserEmail(final String email);
}
