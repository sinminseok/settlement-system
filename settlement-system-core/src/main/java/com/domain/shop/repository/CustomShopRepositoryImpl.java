package com.domain.shop.repository;

import com.domain.shop.entity.QShop;
import com.domain.shop.entity.Shop;
import com.domain.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor

public class CustomShopRepositoryImpl implements CustomShopRepository {

    private final JPAQueryFactory query;

    @Override
    public List<Shop> findByEmailOrOwnerName(String email, String shopName) {
        QUser user = QUser.user;
        QShop shop = QShop.shop;
        BooleanBuilder builder = new BooleanBuilder();
        if (email != null) {
            builder.or(user.email.eq(email));
        }
        if (shopName != null) {
            builder.or(shop.name.eq(shopName));
        }
        List<UUID> userIds = query.select(user.id)
                .from(user)
                .where(builder)
                .fetch();
        if (userIds.isEmpty()) {
            return List.of();
        }
        return query.selectFrom(shop)
                .where(shop.user.id.in(userIds))
                .fetch();
    }

    @Override
    public Shop findByUserEmail(String email) {
        QUser user = QUser.user;
        QShop shop = QShop.shop;

        UUID userId = query.select(user.id)
                .from(user)
                .where(user.email.eq(email))
                .fetchOne();
        if (userId == null) {
            return null;
        }
        Shop shop1 = query.selectFrom(shop)
                .where(shop.user.id.eq(userId))
                .fetchOne();
        return shop1;
    }
}
