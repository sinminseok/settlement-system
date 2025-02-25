package com.domain.shop.service;

import com.domain.shop.dto.ShopResponse;
import com.domain.shop.entity.Shop;
import com.domain.shop.repository.ShopRepository;
import com.utils.OptionalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    public UUID getShopIdByEmail(String email){
        Shop shop = OptionalUtil.getOrElseThrow(shopRepository.findByUserEmail(email), "가게가 존재하지 않습니다.");
        return shop.getId();
    }

    public List<ShopResponse> getAll(){
        return shopRepository.findAll()
                .stream()
                .map(ShopResponse::from)
                .collect(Collectors.toList());
    }

    public List<ShopResponse> getShopByPage(int page, int size){
        Pageable pageable = PageRequest.of(page, size); // 정렬 기준 추가
        Page<Shop> all = shopRepository.findAll(pageable);
        return all.stream().map(ShopResponse::from).toList();
    }

    public List<ShopResponse> getByEmailOrName(final String email, String shopName){
        List<Shop> byEmailOrOwnerName = shopRepository.findByEmailOrOwnerName(email, shopName);
        return byEmailOrOwnerName.stream().map(ShopResponse::from).toList();
    }

}
