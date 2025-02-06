package com.domain.order.repository;


import com.domain.order.entity.Order;
import com.domain.order.entity.QOrder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomOrderRepositoryImpl implements CustomOrderRepository{

    private final JPAQueryFactory query;
    private QOrder qOrder = QOrder.order;

    @Override
    public List<Order> findByShopIdAndPage(UUID shopId, Pageable pageable) {
        return query.selectFrom(qOrder)
                .where(qOrder.shop.id.eq(shopId))
                .orderBy(qOrder.startDateTime.desc())  // 최신 거래 순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
