package com.v1.user;

import com.auth.dto.LoginCommandDto;
import com.auth.dto.LoginResultDto;
import com.auth.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final LoginService loginService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("email") String email,
                                   @RequestParam("password") String password,
                                   HttpServletResponse response) {

            LoginCommandDto request = new LoginCommandDto(email, password);
            LoginResultDto loginResult = loginService.login(request);

            log.info("user {} logged in", email);

            // 쿠키 설정
            response.addHeader(HttpHeaders.SET_COOKIE, loginResult.getAccessTokenCookie().toString());
            response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + loginResult.getRefreshToken());

            // 로그인 성공 시 홈으로 리다이렉트
            return ResponseEntity.ok().header(HttpHeaders.LOCATION, "/home").build();

    }


}
