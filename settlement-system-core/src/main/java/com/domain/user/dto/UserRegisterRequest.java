package com.domain.user.dto;

import com.domain.user.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class UserRegisterRequest {

    private final String email;

    private final String password;

    private final Role role;

    @Builder
    public UserRegisterRequest(String email, String password, Role role){
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
