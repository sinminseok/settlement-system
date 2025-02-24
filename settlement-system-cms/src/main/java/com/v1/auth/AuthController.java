package com.v1.auth;

import com.auth.dto.LoginCommandDto;
import com.auth.dto.LoginResponse;
import com.auth.service.LoginService;
import com.v1.response.SuccessResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginCommandDto request, HttpServletResponse response) {
        // 로그인 서비스 호출
        LoginResponse loginResponse = loginService.login(request);
        // 성공 응답 반환
        SuccessResponse successResponse = new SuccessResponse(true, "로그인 성공", loginResponse);
        return ResponseEntity.ok(successResponse);
    }


}
