package com.writer;


import com.entity.NormalizedTransaction;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class MemoryItemWriter implements ItemWriter<NormalizedTransaction> {

    private final Map<Long, List<NormalizedTransaction>> normalizedTransactionMap;

    public MemoryItemWriter(Map<Long, List<NormalizedTransaction>> normalizedTransactionMap) {
        this.normalizedTransactionMap = normalizedTransactionMap;
    }

    @Override
    public void write(Chunk<? extends NormalizedTransaction> chunk) throws Exception {
        List<NormalizedTransaction> items = (List<NormalizedTransaction>) chunk.getItems(); // Chunk에서 아이템을 가져옴
        for (NormalizedTransaction transaction : items) {
            Long shopId = transaction.getShopId();
            // shopId에 해당하는 리스트가 없으면 새로 생성
            normalizedTransactionMap.computeIfAbsent(shopId, k -> new ArrayList<>()).add(transaction);
        }
    }
}