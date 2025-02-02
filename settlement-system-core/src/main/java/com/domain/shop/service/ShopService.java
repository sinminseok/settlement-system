package com.domain.shop.service;

import com.domain.shop.dto.ShopResponse;
import com.domain.shop.entity.Shop;
import com.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    public List<ShopResponse> findAll(){
        return shopRepository.findAll()
                .stream()
                .map(shop -> toResponse(shop))
                .collect(Collectors.toList());
    }

    private ShopResponse toResponse(Shop shop){
        return ShopResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .build();
    }
}
