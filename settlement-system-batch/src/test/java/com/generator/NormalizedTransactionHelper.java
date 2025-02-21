package com.generator;

import com.domain.order.constants.DiscountType;
import com.domain.order.entity.OrderTransaction;
import com.domain.order.constants.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NormalizedTransactionHelper {

    public static List<OrderTransaction> createNormalizedTransactions(UUID shopId, String shopName, LocalDateTime completionDateTime){
        List<OrderTransaction> response = new ArrayList<>();

        for(int i=0; i<10; i++) {
            response.add(OrderTransaction.builder()
                    .shopName(shopName)
                    .shopId(shopId)
                    .price(10000)
                    .discountType(DiscountType.VIP_DISCOUNT)
                    .completionDateTime(completionDateTime)
                    .status(OrderStatus.COMPLEMENT)
                    .build());
        }

        return response;

    }
}
