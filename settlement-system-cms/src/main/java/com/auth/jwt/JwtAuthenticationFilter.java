package com.auth.jwt;

import java.io.IOException;
import java.util.List;

import com.auth.FilterExceptionResolver;
import com.auth.RequestMatcherHolder;
import com.auth.constants.JwtMetadata;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
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

    private final JwtService jwtService;
    private final FilterExceptionResolver<JwtException> jwtFilterExceptionResolver;
    private final RequestMatcherHolder requestMatcherHolder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            final String accessToken = getAccessTokenFromCookie(request);
            Claims claims = jwtService.verifyToken(accessToken);
            AccessTokenPayload accessTokenPayload = jwtService.createAccessTokenPayload(claims);
            var email = accessTokenPayload.email();
            var role = accessTokenPayload.roleEnum().name();
            GrantedAuthority authority = new SimpleGrantedAuthority(role);
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, List.of(authority));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (JwtException ex) {
            logger.info("Failed to authorize/authenticate with JWT due to " + ex.getMessage());
            jwtFilterExceptionResolver.setResponse(response, ex);
        }

    }

    private String getAccessTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new JwtException("Missing cookie");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(JwtMetadata.ACCESS_TOKEN)) {
                if (cookie.getValue() == null || cookie.getValue().isEmpty()) {
                    throw new JwtException("Empty Token in Cookie");
                }
                return cookie.getValue();
            }
        }
        throw new JwtException("Missing Token");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        RequestMatcher requestMatchers = requestMatcherHolder.getRequestMatchersByMinRole(null);
        return requestMatchers.matches(request);
    }
}