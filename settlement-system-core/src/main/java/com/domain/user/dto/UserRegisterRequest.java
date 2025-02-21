package com.domain.user.dto;

import com.domain.user.entity.Role;
import com.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class UserRegisterRequest {

    private final String email;

    private final String password;

    private final Role role;

    public User toEntity(String encodePassword){
        return User.builder()
                .email(this.getEmail())
                .password(encodePassword)
                .role(this.getRole())
                .build();
    }

    @Builder
    public UserRegisterRequest(String email, String password, Role role){
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
