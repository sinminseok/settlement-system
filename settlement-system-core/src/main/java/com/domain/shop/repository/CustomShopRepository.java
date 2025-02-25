package com.domain.shop.repository;

import com.domain.shop.entity.Shop;

import java.util.List;
import java.util.Optional;

public interface CustomShopRepository {

    List<Shop> findByEmailOrOwnerName(final String email, final String ownerName);

    Optional<Shop> findByUserEmail(final String email);
}
