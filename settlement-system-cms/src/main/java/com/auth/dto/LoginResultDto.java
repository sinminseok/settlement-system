package com.auth.dto;


import com.domain.user.entity.User;
import org.springframework.http.ResponseCookie;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class LoginResultDto {

    private String refreshToken;
    private User user;
    private ResponseCookie accessTokenCookie;

    @Builder
    public LoginResultDto(ResponseCookie responseCookie, String refreshToken, User user) {
        this.refreshToken = refreshToken;
        this.user = user;
        this.accessTokenCookie = responseCookie;
    }

    public static LoginResponseDto convertFromDto(LoginResultDto dto) {
        return new LoginResponseDto(dto.user.getEmail(), dto.user.getRole(), dto.user.getId());
    }
}