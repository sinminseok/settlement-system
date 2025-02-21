package com.auth.service;

import java.time.Duration;

import com.auth.dto.JwtMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Value("${jwt.access-key-expiration-s}")
    private long accessKeyExpirationInS;

    @Value("${jwt.refresh-key-expiration-s}")
    private long refreshKeyExpirationInS;

    @Value("${server.servlet.context-path}")
    private String contextPath; // contextPath = /

    public ResponseCookie createAccessTokenCookie(String accessToken) {
        return ResponseCookie.from(JwtMetadata.ACCESS_TOKEN, accessToken)
                .httpOnly(true)
                .secure(false)
                .maxAge(accessKeyExpirationInS)
                .path(contextPath)
                .sameSite("None")
                .build();
    }

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(JwtMetadata.REFRESH_TOKEN, refreshToken)
                .httpOnly(true)
                .secure(false)
                .maxAge(refreshKeyExpirationInS)
                .path(contextPath)
                .sameSite("None")
                .build();
    }

}
