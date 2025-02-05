package com.domain.user.service;

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

    public void register(UserRegisterRequest userRegisterRequest, String encodePassword){
        System.out.println(userRegisterRequest.getRole());
        User user = User.builder()
                .email(userRegisterRequest.getEmail())
                .password(encodePassword)
                .role(userRegisterRequest.getRole())
                .build();
        userRepository.save(user);
    }


}
