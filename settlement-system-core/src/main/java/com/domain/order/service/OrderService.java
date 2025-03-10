package com.domain.order.service;

import com.domain.order.dto.OrderFilterRequest;
import com.domain.order.dto.OrderPatchRequest;
import com.domain.order.dto.OrderResponse;
import com.domain.order.entity.Order;
import com.domain.order.repository.OrderRepository;
import com.utils.OptionalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

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

    public Integer getPeriodCount(final UUID shopId, final LocalDate startDate, final LocalDate endDate){
        return orderRepository.countByPeriod(shopId, startDate, endDate);
    }

    public Integer getOrderCount(final UUID shopId) {
        return orderRepository.countByShopId(shopId);
    }

    public Integer getOrderCountByFiler(final OrderFilterRequest orderFilterRequest){
        return orderRepository.countByPeriodAndStatus(orderFilterRequest.getOrderStatus(), orderFilterRequest.getStartTime(), orderFilterRequest.getEndTime());
    }

    public Integer getTodayOrderCount(final UUID shopId) {
        return orderRepository.findOrderCount(shopId, LocalDate.now());
    }

    public List<OrderResponse> getOrdersByPeriod(final UUID shopId, final int page, final int size,final LocalDate startDate, final LocalDate endDate){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDateTime"));
        List<Order> orders = orderRepository.findByShopIdAndPeriod(shopId, pageable, startDate, endDate);
        return orders.stream().map(OrderResponse::from).toList();
    }

    public List<OrderResponse> getRecentOrders(final UUID shopId) {
        List<Order> orders = orderRepository.findRecentOrders(shopId, LocalDate.now());
        return orders.stream().map(OrderResponse::from).toList();
    }

    public List<OrderResponse> getOrdersByFilter(int page, int size, final OrderFilterRequest orderFilterRequest){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDateTime"));
        List<Order> orders = orderRepository.findByFilterAndPage(orderFilterRequest.getOrderStatus(), orderFilterRequest.getStartTime(), orderFilterRequest.getEndTime(), pageable);
        return orders.stream().map(OrderResponse::from).toList();
    }

    @Transactional
    public void changeStatus(final OrderPatchRequest orderPatchRequest) {
        Order order = OptionalUtil.getOrElseThrow(orderRepository.findById(orderPatchRequest.getOrderId()), "존재하지 않는 주문 ID 입니다.");
        order.setStatus(orderPatchRequest.getOrderStatus());
    }

    @Transactional
    public void deleteById(final UUID orderId) {
        orderRepository.deleteById(orderId);
    }


}
