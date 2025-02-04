package com.v1.shop;

import com.auth.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeViewController {

    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<String> home(@CookieValue(name = "accessToken", defaultValue = "") String accessToken) {
        if (accessToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없습니다.");
        }

//        // 토큰을 이용한 인증 처리
//        boolean isValid = jwtService.verifyToken(accessToken);
//        if (!isValid) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
//        }

        // 정상적인 요청 처리
        return ResponseEntity.ok("홈 데이터");
    }
}
