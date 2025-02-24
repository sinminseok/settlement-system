package com.v1.owner;

import com.auth.SecurityContextHelper;
import com.domain.order.dto.InitOrderResponse;
import com.domain.order.dto.OrderResponse;
import com.domain.order.service.OrderService;
import com.domain.settlement.dto.SettlementResponse;
import com.domain.settlement.entity.Settlement;
import com.domain.settlement.service.SettlementService;
import com.domain.shop.service.ShopService;
import com.domain.user.dto.OwnerDashBoardResponse;
import com.domain.user.service.UserService;
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
@RequestMapping("/owners")
public class OwnerController {

    private final SecurityContextHelper securityContextHelper;
    private final OrderService orderService;
    private final ShopService shopService;
    private final SettlementService settlementService;

    @GetMapping("/dash-board")
    public ResponseEntity<?> getDashBoard() {
        String emailInToken = securityContextHelper.getEmailInToken();
        UUID shopId = shopService.getShopIdByEmail(emailInToken);
        OwnerDashBoardResponse ownerDashBoardResponse = getOwnerDashBoardResponse(shopId);
        SuccessResponse response = new SuccessResponse(true, "Owner 대시보드 정보 조회", ownerDashBoardResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/shopId")
    public ResponseEntity<?> getShopId() {
        String emailInToken = securityContextHelper.getEmailInToken();
        UUID shopUUID = shopService.getShopIdByEmail(emailInToken);
        SuccessResponse response = new SuccessResponse(true, "가게 ID 조회 성공", shopUUID);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private OwnerDashBoardResponse getOwnerDashBoardResponse(UUID shopId) {
        return OwnerDashBoardResponse.builder()
                .shopId(shopId)
                .recentOrders(orderService.getRecentOrders(shopId))
                .todaySettlement(SettlementResponse.from(settlementService.findByIdAndDate(shopId, LocalDate.now())))
                .todayOrderCount(orderService.getTodayOrderCount(shopId))
                .weeklySales(settlementService.getWeeklySales(shopId))
                .build();
    }
}
