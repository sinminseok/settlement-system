package com.domain.user.service;

import com.domain.shop.entity.Shop;
import com.domain.shop.repository.ShopRepository;
import com.domain.user.dto.UserRegisterRequest;
import com.domain.user.entity.Role;
import com.domain.user.entity.User;
import com.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;

    public void register(UserRegisterRequest userRegisterRequest, String encodePassword){
        User user = userRepository.save(userRegisterRequest.toEntity(encodePassword));
        //todo 삭제 예정
        Shop shop = Shop.builder()
                .name("민석이네 치킨")
                .user(user)
                .build();
        shopRepository.save(shop);
    }





}
