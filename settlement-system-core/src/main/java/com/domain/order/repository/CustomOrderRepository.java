package com.domain.order.repository;

import com.domain.order.constants.OrderStatus;
import com.domain.order.entity.Order;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CustomOrderRepository {
    List<Order> findByShopIdAndPage(UUID shopId, Pageable pageable);

    List<Order> findByShopIdAndPeriod(UUID shopId, Pageable pageable, LocalDate startDate, LocalDate endDate);

    List<Order> findRecentOrders(UUID shopId, LocalDate today);

    List<Order> findByFilterAndPage(OrderStatus orderStatus, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Integer findOrderCount(UUID shopId, LocalDate date);

    int countByShopId(UUID shopId);

    int countByPeriodAndStatus(OrderStatus orderStatus, LocalDate startDate, LocalDate endDate);

    int countByPeriod(UUID shopId, LocalDate startDate, LocalDate endDate);
}
