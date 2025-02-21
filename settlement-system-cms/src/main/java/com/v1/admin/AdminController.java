package com.v1.admin;

import com.domain.order.dto.OrderResponse;
import com.domain.order.service.OrderService;
import com.domain.shop.dto.ShopResponse;
import com.domain.shop.service.ShopService;
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
@RequestMapping("/admins")
public class AdminController {

    private final ShopService shopService;
    private final OrderService orderService;

    @GetMapping("/shops")
    public ResponseEntity<?> getShops(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        List<ShopResponse> shopByPage = shopService.getShopByPage(page, size);
        SuccessResponse response = new SuccessResponse(true, "가게 페이징 조회 성공", shopByPage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/shops/id")
    public ResponseEntity<?> getShopInformation(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name
    ){
        List<ShopResponse> byEmailOrName = shopService.getByEmailOrName(email, name);
        SuccessResponse response = new SuccessResponse(true, "가게 조회 성공", byEmailOrName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/shops/orders")
    public ResponseEntity<?> getOrdersByShopId(
            @RequestParam UUID shopId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        List<OrderResponse> orders = orderService.getOrdersByPage(shopId, page, size);
        SuccessResponse response = new SuccessResponse(true, "주문 페이징 조회 성공", orders);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
