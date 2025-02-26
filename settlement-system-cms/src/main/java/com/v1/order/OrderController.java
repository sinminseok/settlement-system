package com.v1.order;

import com.domain.order.dto.InitOrderResponse;
import com.domain.order.dto.OrderPatchRequest;
import com.domain.order.dto.OrderResponse;
import com.domain.order.service.OrderService;
import com.v1.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(name = "shopId") UUID shopId
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
            @RequestParam(name = "shopId") UUID shopId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        List<OrderResponse> orders = orderService.getOrdersByPage(shopId, page, size);
        SuccessResponse response = new SuccessResponse(true, "주문 페이징 조회 성공", orders);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/init/period")
    public ResponseEntity<?> getOrdersByPeriod(
            @RequestParam(name = "shopId") UUID shopId,
            @RequestParam(name = "startDate") LocalDate startDate,
            @RequestParam(name = "endDate") LocalDate endDate
    ) {
        Integer totalCount = orderService.getPeriodCount(shopId, startDate, endDate);
        List<OrderResponse> orders = orderService.getOrdersByPeriod(shopId, 0, 10, startDate, endDate);
        InitOrderResponse initOrderResponse = InitOrderResponse.builder()
                .orderCount(totalCount)
                .orderResponses(orders)
                .build();
        SuccessResponse response = new SuccessResponse(true, "기간에 포함된 초기 주문 조회 성공", initOrderResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/period")
    public ResponseEntity<?> getOrdersByPeriod(
            @RequestParam(name = "shopId") UUID shopId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "startDate") LocalDate startDate,
            @RequestParam(name = "endDate") LocalDate endDate
    ) {
        List<OrderResponse> orders = orderService.getOrdersByPeriod(shopId, page, size, startDate, endDate);
        SuccessResponse response = new SuccessResponse(true, "기간에 포함된 주문 페이징 조회 성공", orders);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<?> changeOrderStatus(@RequestBody OrderPatchRequest orderPatchRequest) {
        orderService.changeStatus(orderPatchRequest);
        SuccessResponse response = new SuccessResponse(true, "주문 상태 변경 성공", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<?> changeOrderStatus(@RequestParam(name = "orderId") UUID orderId) {
        orderService.deleteById(orderId);
        SuccessResponse response = new SuccessResponse(true, "주문 삭제", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
