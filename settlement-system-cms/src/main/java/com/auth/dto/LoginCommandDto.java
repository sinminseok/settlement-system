package com.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class LoginCommandDto {
    private final String email;

    private final String password;

    @Builder
    public LoginCommandDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}