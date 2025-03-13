package com.etl;

import com.domain.order.entity.OrderTransaction;
import com.domain.order.entity.Order;
import com.domain.order.constants.OrderStatus;
import com.domain.order.entity.QOrder;
import com.parameters.DateParameter;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reader.QuerydslPagingItemReader;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;

import java.time.LocalDate;
import java.util.function.Function;

public class DataCollectionComponents {

    public static ItemReader<Order> dataCollectionReader(EntityManagerFactory entityManagerFactory, DateParameter dateParameter) {
        LocalDate requestDate = dateParameter.getRequestDate();
        QOrder qOrder = QOrder.order;

        Function<JPAQueryFactory, JPAQuery<Order>> queryFunction = queryFactory ->
                queryFactory.selectFrom(qOrder)
                        .where(qOrder.completionDateTime.year().eq(requestDate.getYear())
                                .and(qOrder.completionDateTime.month().eq(requestDate.getMonthValue()))
                                .and(qOrder.completionDateTime.dayOfMonth().eq(requestDate.getDayOfMonth())));

        return new QuerydslPagingItemReader<>(entityManagerFactory, 100, queryFunction);
    }


    public static ItemProcessor<Order, OrderTransaction> dataCollectionProcessor() {
        return transaction -> {
            if (!validateTransaction(transaction)) return null;
            return toNormalizedTransaction(transaction);
        };
    }

    public static JpaItemWriter<OrderTransaction> dataCollectionWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<OrderTransaction> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    private static boolean validateTransaction(Order transaction) {
        if (transaction.getCompletionDateTime() != null && transaction.getStatus() == OrderStatus.COMPLEMENT && transaction.getPrice() >= 0) {
            return true;
        }
        return false;
    }

    private static OrderTransaction toNormalizedTransaction(Order transaction){
        return OrderTransaction.builder()
                .price(transaction.getPrice())
                .discountType(transaction.getDiscountType())
                .completionDateTime(transaction.getCompletionDateTime())
                .shopId(transaction.getShop().getId())
                .status(transaction.getStatus())
                .shopName(transaction.getShop().getName())
                .build();
    }
}
