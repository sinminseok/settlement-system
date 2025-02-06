package com.domain.order.repository;


import com.domain.order.entity.Order;
import com.domain.order.entity.QOrder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Override
    public List<Order> findByShopIdAndPeriod(UUID shopId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();  // startDate의 시작 시간
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);  // endDate의 끝 시간
        return query.selectFrom(qOrder)
                .where(qOrder.shop.id.eq(shopId)  // shopId 필터링
                        .and(qOrder.startDateTime.between(startDateTime, endDateTime)))  // startDateTime이 startDate와 endDate 사이에 포함되는지 확인
                .orderBy(qOrder.startDateTime.desc())  // 최신 거래 순 정렬
                .fetch();
    }
}
