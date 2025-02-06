package com.domain.order.repository;

import com.domain.order.entity.Order;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CustomOrderRepository {
    List<Order> findByShopIdAndPage(UUID shopId, Pageable pageable);

    List<Order> findByShopIdAndPeriod(UUID shopId, LocalDate startDate, LocalDate endDate);
}
