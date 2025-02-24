package com.auth.dto;


import com.domain.user.entity.Role;
import org.springframework.http.ResponseCookie;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class LoginResponse {
    private Role role;
    private String refreshToken;
    private String accessToken;

    @Builder
    public LoginResponse(Role role, String accessToken, String refreshToken) {
        this.role = role;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }
}