package com.v1.user;

import com.domain.user.dto.UserRegisterRequest;
import com.domain.user.service.UserService;
import com.v1.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest request){
        userService.register(request, passwordEncoder.encode(request.getPassword()));
        SuccessResponse response = new SuccessResponse(true, "회원 등록 성공", null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
