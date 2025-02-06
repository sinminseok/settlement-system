package com.domain.order.repository;

import org.springframework.data.domain.Pageable;
import com.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, CustomOrderRepository {
}
