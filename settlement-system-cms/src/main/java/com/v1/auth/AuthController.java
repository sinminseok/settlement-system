package com.v1.auth;

import com.auth.dto.LoginCommandDto;
import com.auth.dto.LoginResponse;
import com.auth.service.LoginService;
import com.v1.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginCommandDto request) {
        LoginResponse loginResponse = loginService.login(request);
        SuccessResponse response = new SuccessResponse(true, "로그인 성공", loginResponse.getRole());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, loginResponse.getRefreshTokenCookie().toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getAccessTokenCookie().toString())
                .body(response);
    }
}
