package com.domain.order.repository;

import com.domain.order.entity.Order;

import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CustomOrderRepository {
    List<Order> findByShopIdAndPage(UUID shopId, Pageable pageable);

    List<Order> findByShopIdAndPeriod(UUID shopId, Pageable pageable, LocalDate startDate, LocalDate endDate);

    List<Order> findRecentOrders(UUID shopId, LocalDate today);

    Integer findOrderCount(UUID shopId, LocalDate date);

    int countByShopId(UUID shopId);

    int countByPeriod(UUID shopId, LocalDate startDate, LocalDate endDate);
}
