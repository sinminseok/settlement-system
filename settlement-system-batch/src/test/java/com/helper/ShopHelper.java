package com.helper;

import com.entity.Shop;
import com.entity.Transaction;
import com.entity.TransactionStatus;

import java.time.LocalDateTime;

public class ShopHelper {

    public static Shop createShop(String shopName,LocalDateTime startDateTime,LocalDateTime cancelDateTime ,LocalDateTime completionDateTime){
        Shop shop = Shop.builder()
                .name(shopName)
                .build();

        for(int i=0; i< 10; i++) {
            Transaction transaction = Transaction.builder()
                    .price(1000 + i*1000)
                    .status(TransactionStatus.COMPLEMENT)
                    .startDateTime(startDateTime)
                    .cancelDateTime(cancelDateTime)
                    .completionDateTime(completionDateTime)
                    .build();
            shop.addTransaction(transaction);
        }
        return shop;
    }
}
