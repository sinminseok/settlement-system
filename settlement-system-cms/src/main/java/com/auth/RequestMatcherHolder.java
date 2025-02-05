package com.auth;

import static org.springframework.http.HttpMethod.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.domain.user.entity.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;


import jakarta.annotation.Nullable;

@Component
public class RequestMatcherHolder {

    private static final List<RequestInfo> REQUEST_INFO_LIST = List.of(
            new RequestInfo(GET, "/css/*", null),
            //회원가입, 로그인
            new RequestInfo(GET, "/auth/*", null),
            new RequestInfo(POST, "/auth/*", null),
            new RequestInfo(GET, "/users/*", null),
            // admin (관리자)
            new RequestInfo(POST, "/admin/*", Role.ADMIN),
            new RequestInfo(GET, "/admin/*", Role.ADMIN),
            // owner (가게 사장)
            new RequestInfo(POST, "/owner/*", Role.OWNER),
            new RequestInfo(GET, "/owner/*", Role.OWNER)
    );

    private final ConcurrentHashMap<String, RequestMatcher> reqMatcherCacheMap = new ConcurrentHashMap<>();

    /**
     * if role == null, return permitAll Path
     */
    public RequestMatcher getRequestMatchersByMinRole(@Nullable Role minRole) {
        var key = getKeyByRole(minRole);
        if (!reqMatcherCacheMap.containsKey(key)) {
            var requestMatcherByMinRole = new OrRequestMatcher(REQUEST_INFO_LIST.stream()
                    .filter(reqInfo -> reqInfo.minRole() == null || reqInfo.minRole().equals(minRole))
                    .map(reqInfo -> new AntPathRequestMatcher(reqInfo.pattern(), reqInfo.method().name()))
                    .toArray(AntPathRequestMatcher[]::new));
            reqMatcherCacheMap.put(key, requestMatcherByMinRole);
        }
        return reqMatcherCacheMap.get(key);
    }

    private String getKeyByRole(@Nullable Role minRole) {
        if (minRole == null) {
            return "VISITOR";
        }
        return minRole.name();
    }

    private record RequestInfo(HttpMethod method, String pattern, Role minRole) {
    }
}