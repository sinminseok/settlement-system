package com.v1.order;

import com.domain.order.dto.InitOrderResponse;
import com.domain.order.dto.OrderResponse;
import com.domain.order.service.OrderService;
import com.v1.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/init")
    public ResponseEntity<?> getInitOrders(
            @RequestParam UUID shopId
    ) {
        Integer orderCount = orderService.getOrderCount(shopId);
        List<OrderResponse> orders = orderService.getOrdersByPage(shopId, 0, 10);
        InitOrderResponse initOrderResponse = InitOrderResponse.builder()
                .orderCount(orderCount)
                .orderResponses(orders)
                .build();
        SuccessResponse response = new SuccessResponse(true, "첫, 주문 페이징 조회 성공", initOrderResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getOrders(
            @RequestParam UUID shopId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<OrderResponse> orders = orderService.getOrdersByPage(shopId, page, size);
        SuccessResponse response = new SuccessResponse(true, "주문 페이징 조회 성공", orders);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/period")
    public ResponseEntity<?> getOrdersByPeriod(
            @RequestParam UUID shopId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        List<OrderResponse> orders = orderService.getOrdersByPeriod(shopId, startDate, endDate);
        SuccessResponse response = new SuccessResponse(true, "기간에 포함된 주문 조회 성공", orders);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
