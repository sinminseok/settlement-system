package com.auth.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.auth.FilterExceptionResolver;
import com.auth.RequestMatcherHolder;
import com.auth.dto.JwtMetadata;
import com.domain.user.entity.RefreshToken;
import com.domain.user.repository.RefreshTokenRepository;
import com.utils.OptionalUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final FilterExceptionResolver<JwtException> jwtFilterExceptionResolver;
    private final RequestMatcherHolder requestMatcherHolder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            checkAccessToken(request, response, filterChain);
        } catch (JwtException ex) {
            checkRefreshToken(request, response, filterChain);
        }
    }

    private void checkRefreshToken(HttpServletRequest request, HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {
        // AccessToken이 유효하지 않으면 refreshToken을 쿠키에서 추출하여 검증
        String refreshToken = getRefreshTokenFromCookie(request);
        try {
            // RefreshToken을 검증하고, 새로운 AccessToken을 발급
            Claims refreshClaims = jwtService.verifyToken(refreshToken);
            AccessTokenPayload refreshTokenPayload = jwtService.createAccessTokenPayload(refreshClaims);
            String email = refreshTokenPayload.email();
            String role = refreshTokenPayload.roleEnum().name();
            GrantedAuthority authority = new SimpleGrantedAuthority(role);
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, List.of(authority));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            RefreshToken beforeRefreshToken = OptionalUtil.getOrElseThrow(refreshTokenRepository.findById(getRefreshTokenFromCookie(request)), "존재하지 않는 refreshToken 입니다.");
            AccessTokenPayload accessTokenPayload = new AccessTokenPayload(beforeRefreshToken.getUser().getEmail(), beforeRefreshToken.getUser().getRole(), new Date());
            String newAccessToken = jwtService.createAccessToken(accessTokenPayload);
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);
            filterChain.doFilter(request, response);

        } catch (JwtException refreshEx) {
            logger.info("Failed to authorize/authenticate with refresh token due to " + refreshEx.getMessage());
            jwtFilterExceptionResolver.setResponse(response, refreshEx);
        }
    }

    private void checkAccessToken(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        final String accessToken = getAccessTokenFromHeader(request);
        Claims claims = jwtService.verifyToken(accessToken);
        AccessTokenPayload accessTokenPayload = jwtService.createAccessTokenPayload(claims);
        var email = accessTokenPayload.email();
        var role = accessTokenPayload.roleEnum().name();
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, List.of(authority));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    // Authorization 헤더에서 AccessToken 추출
    private String getAccessTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new JwtException("Missing or invalid Authorization header");
        }
        return authorizationHeader.substring(7);  // "Bearer "을 제외한 토큰 값 추출
    }

    // 쿠키에서 RefreshToken 추출
    private String getRefreshTokenFromCookie(HttpServletRequest request) {
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

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        RequestMatcher requestMatchers = requestMatcherHolder.getRequestMatchersByMinRole(null);
        return requestMatchers.matches(request);
    }
}
