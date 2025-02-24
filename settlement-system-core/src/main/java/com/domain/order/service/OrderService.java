package com.domain.order.service;

import com.domain.order.dto.OrderResponse;
import com.domain.order.entity.Order;
import com.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<OrderResponse> getOrdersByPage(final UUID shopId, final int page, final int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDateTime"));
        List<Order> orders = orderRepository.findByShopIdAndPage(shopId, pageable);
        return orders.stream().map(OrderResponse::from).toList();
    }

    public Integer getOrderCount(final UUID shopId) {
        return orderRepository.countByShopId(shopId);
    }

    public Integer getTodayOrderCount(final UUID shopId) {
        return orderRepository.findOrderCount(shopId, LocalDate.now());
    }

    public List<OrderResponse> getOrdersByPeriod(final UUID shopId, final LocalDate startDate, final LocalDate endDate){
        List<Order> orders = orderRepository.findByShopIdAndPeriod(shopId, startDate, endDate);
        return orders.stream().map(OrderResponse::from).toList();
    }

    public List<OrderResponse> getRecentOrders(final UUID shopId) {
        List<Order> orders = orderRepository.findRecentOrders(shopId, LocalDate.now());
        return orders.stream().map(OrderResponse::from).toList();
    }

}
