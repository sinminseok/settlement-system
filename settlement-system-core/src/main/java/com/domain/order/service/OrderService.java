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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<OrderResponse> getOrdersByPage(UUID shopId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDateTime"));
        List<Order> orders = orderRepository.findByShopIdAndPage(shopId, pageable);
        return orders.stream().map(OrderResponse::from).toList();
    }

    public List<OrderResponse> getOrdersByPeriod(UUID shopId, LocalDate startDate, LocalDate endDate){
        List<Order> orders = orderRepository.findByShopIdAndPeriod(shopId, startDate, endDate);
        return orders.stream().map(OrderResponse::from).toList();
    }



}
