package com.helper;

import com.entity.DiscountType;
import com.entity.NormalizedTransaction;
import com.entity.TransactionStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NormalizedTransactionHelper {

    public static List<NormalizedTransaction> createNormalizedTransactions(Long shopId, String shopName, LocalDateTime completionDateTime){
        List<NormalizedTransaction> response = new ArrayList<>();

        for(int i=1; i<10; i++) {
            response.add(NormalizedTransaction.builder()
                    .shopName(shopName)
                    .shopId(shopId)
                    .price(1000 * i)
                    .discountType(DiscountType.VIP_DISCOUNT)
                    .completionDateTime(completionDateTime)
                    .status(TransactionStatus.COMPLEMENT)
                    .build());
        }

        return response;

    }
}
