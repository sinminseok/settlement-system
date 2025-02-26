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
                .orderBy(qOrder.startDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Order> findByShopIdAndPeriod(UUID shopId, Pageable pageable,LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        return query.selectFrom(qOrder)
                .where(qOrder.shop.id.eq(shopId)
                        .and(qOrder.startDateTime.between(startDateTime, endDateTime)))
                .orderBy(qOrder.startDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Order> findRecentOrders(UUID shopId, LocalDate today) {
        LocalDateTime now = today.atTime(23, 59, 59);
        return query.selectFrom(qOrder)
                .where(qOrder.shop.id.eq(shopId)
                        .and(qOrder.startDateTime.loe(now)))
                .orderBy(qOrder.startDateTime.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public Integer findOrderCount(UUID shopId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return query.select(qOrder.count())
                .from(qOrder)
                .where(
                        qOrder.shop.id.eq(shopId),
                        qOrder.startDateTime.between(startOfDay, endOfDay)
                )
                .fetchOne()
                .intValue();
    }

    @Override
    public int countByShopId(UUID shopId) {
        Long count = query.select(qOrder.count())
                .from(qOrder)
                .where(qOrder.shop.id.eq(shopId))
                .fetchOne();
        return (count != null) ? count.intValue() : 0;
    }

    @Override
    public int countByPeriod(UUID shopId, LocalDate startDate, LocalDate endDate) {
        // 시작 날짜와 종료 날짜를 LocalDateTime으로 변환
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // 하루의 마지막 시간까지 포함

        // 기간에 해당하는 주문 개수 카운트
        Long count = query.select(qOrder.count())
                .from(qOrder)
                .where(qOrder.shop.id.eq(shopId)
                        .and(qOrder.startDateTime.between(startDateTime, endDateTime)))
                .fetchOne();

        return (count != null) ? count.intValue() : 0;
    }



}
