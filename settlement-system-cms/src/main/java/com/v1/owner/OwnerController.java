package com.v1.owner;

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

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owners")
public class OwnerController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(
            @RequestParam UUID shopId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<OrderResponse> orders = orderService.getOrdersByPage(shopId, page, size);
        SuccessResponse response = new SuccessResponse(true, "주문 페이징 조회 성공", orders);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
