package com.etl;

import com.entity.NormalizedTransaction;
import com.entity.Transaction;
import com.entity.TransactionStatus;
import com.parameters.DateParameter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;

import java.time.LocalDate;
import java.util.Map;

public class DataCollectionComponents {

    public static JpaPagingItemReader<Transaction> dataCollectionReader(EntityManagerFactory entityManagerFactory, DateParameter dateParameter) {
        LocalDate requestDate = dateParameter.getRequestDate();
        String query = "SELECT t FROM Transaction t WHERE FUNCTION('DATE', t.completionDateTime) = :requestDate";

        return new JpaPagingItemReaderBuilder<Transaction>()
                .name("dataCollectionReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .queryString(query)
                .parameterValues(Map.of("requestDate", requestDate))
                .build();
    }

    public static ItemProcessor<Transaction, NormalizedTransaction> dataCollectionProcessor() {
        return transaction -> {
            if (!validateTransaction(transaction)) return null;
            return toNormalizedTransaction(transaction);
        };
    }

    public static JpaItemWriter<NormalizedTransaction> dataCollectionWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<NormalizedTransaction> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    private static boolean validateTransaction(Transaction transaction) {
        //날짜 데이터 누락, 거래 상태, 거래 금액 양수 확인
        if (transaction.getCompletionDateTime() != null && transaction.getStatus() == TransactionStatus.COMPLEMENT && transaction.getPrice() >= 0) {
            return true;
        }
        return false;
    }

    private static NormalizedTransaction toNormalizedTransaction(Transaction transaction){
        return NormalizedTransaction.builder()
                .price(transaction.getPrice())
                .discountType(transaction.getDiscountType())
                .completionDateTime(transaction.getCompletionDateTime())
                .shopId(transaction.getShop().getId())
                .status(transaction.getStatus())
                .shopName(transaction.getShop().getName())
                .build();
    }
}
