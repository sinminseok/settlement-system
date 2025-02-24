package com.auth.jwt;

import com.auth.dto.JwtMetadata;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public class JwtExtractor {

    public static String extractAccessTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new JwtException("Missing or invalid Authorization header");
        }
        return authorizationHeader.substring(7);
    }

    public static String extractRefreshTokenFromHeader(HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh-Token");
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new JwtException("Missing or invalid Refresh-Token header");
        }
        return refreshToken;
    }


    public static String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new JwtException("Missing cookies");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(JwtMetadata.REFRESH_TOKEN)) {
                if (cookie.getValue() == null || cookie.getValue().isEmpty()) {
                    throw new JwtException("Empty refresh token in cookie");
                }
                return cookie.getValue();
            }
        }
        throw new JwtException("Missing refresh token");
    }
}
