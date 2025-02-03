package com.auth;

import static org.springframework.http.HttpMethod.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.domain.user.entity.RoleEnum;
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
            // admin (관리자)
            new RequestInfo(POST, "/admin/register", null),
            // owner (가게 사장)
            new RequestInfo(POST, "/owner/register", null)

    );

    private final ConcurrentHashMap<String, RequestMatcher> reqMatcherCacheMap = new ConcurrentHashMap<>();

    /**
     * if role == null, return permitAll Path
     */
    public RequestMatcher getRequestMatchersByMinRole(@Nullable RoleEnum minRole) {
        var key = getKeyByRole(minRole);
        if (!reqMatcherCacheMap.containsKey(key)) {
            var matchers = REQUEST_INFO_LIST.stream()
                    .filter(reqInfo -> Objects.equals(reqInfo.minRole(), minRole))
                    .map(reqInfo -> new AntPathRequestMatcher(reqInfo.pattern(), reqInfo.method().name()))
                    .toArray(AntPathRequestMatcher[]::new);

            if (matchers.length == 0) {
                reqMatcherCacheMap.put(key, AnyRequestMatcher.INSTANCE); // 빈 경우 기본 matcher 설정
            } else {
                reqMatcherCacheMap.put(key, new OrRequestMatcher(matchers));
            }
        }
        return reqMatcherCacheMap.get(key);
    }

    private String getKeyByRole(@Nullable RoleEnum minRole) {
        if (minRole == null) {
            return "VISITOR";
        }
        return minRole.name();
    }

    private record RequestInfo(HttpMethod method, String pattern, RoleEnum minRole) {
    }
}