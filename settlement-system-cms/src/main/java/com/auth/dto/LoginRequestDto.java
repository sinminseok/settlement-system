package com.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class LoginRequestDto {

    private final String email;

    private final String password;

    @Builder
    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginCommandDto toCommand() {
        return new LoginCommandDto(email, password);
    }
}
