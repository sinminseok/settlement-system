package com.auth.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.auth.FilterExceptionResolver;
import com.auth.RequestMatcherHolder;
import com.auth.dto.LoginResponse;
import com.domain.user.entity.RefreshToken;
import com.domain.user.entity.User;
import com.domain.user.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utils.OptionalUtil;
import com.v1.response.SuccessResponse;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.auth.jwt.JwtExtractor.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final FilterExceptionResolver<JwtException> jwtFilterExceptionResolver;
    private final RequestMatcherHolder requestMatcherHolder;
    ObjectMapper objectMapper = new ObjectMapper();

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
        String refreshToken = extractRefreshTokenFromHeader(request);
        try {
            jwtService.verifyToken(refreshToken);
            RefreshToken beforeRefreshToken = OptionalUtil.getOrElseThrow((refreshTokenRepository.findByToken(refreshToken)), "존재하지 않는 refreshToken 입니다.");
            User user = beforeRefreshToken.getUser();
            GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, List.of(authority));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            AccessTokenPayload accessTokenPayload = new AccessTokenPayload(beforeRefreshToken.getUser().getEmail(), beforeRefreshToken.getUser().getRole(), new Date());
            String newAccessToken = jwtService.createAccessToken(accessTokenPayload);
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);
            LoginResponse loginResponse = LoginResponse.builder()
                    .refreshToken(beforeRefreshToken.getToken())
                    .role(user.getRole())
                    .accessToken(newAccessToken)
                    .build();
            SuccessResponse successResponse = new SuccessResponse(true, "토큰 재발급", loginResponse);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            String jsonResponse = objectMapper.writeValueAsString(successResponse);
            response.getWriter().write(jsonResponse);
        } catch (JwtException refreshEx) {
            jwtFilterExceptionResolver.setResponse(response, refreshEx);
        }
    }

    private void checkAccessToken(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        final String accessToken = extractAccessTokenFromHeader(request);
        Claims claims = jwtService.verifyToken(accessToken);
        AccessTokenPayload accessTokenPayload = jwtService.createAccessTokenPayload(claims);
        var email = accessTokenPayload.email();
        var role = accessTokenPayload.roleEnum().name();
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, List.of(authority));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        RequestMatcher requestMatchers = requestMatcherHolder.getRequestMatchersByMinRole(null);
        return requestMatchers.matches(request);
    }
}
