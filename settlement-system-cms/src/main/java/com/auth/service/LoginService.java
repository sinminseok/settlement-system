package com.auth.service;

import com.auth.dto.LoginCommandDto;
import com.auth.dto.LoginResponse;
import com.auth.jwt.AccessTokenPayload;
import com.auth.jwt.JwtService;
import com.auth.jwt.RefreshTokenPayload;
import com.domain.user.entity.RefreshToken;
import com.domain.user.entity.User;
import com.domain.user.repository.RefreshTokenRepository;
import com.domain.user.repository.UserRepository;
import com.exception.EmailExistException;
import com.exception.ErrorResponseCode;
import com.exception.PasswordMatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieService cookieService;

    @Transactional
    public LoginResponse login(LoginCommandDto command){
        User user = getValidatedUser(command.getEmail(), command.getPassword());
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);
        RefreshToken saved = refreshTokenRepository.save(RefreshToken.builder()
                        .user(user)
                .build());

        AccessTokenPayload accessTokenPayload = new AccessTokenPayload(user.getEmail(), user.getRole(), new Date());
        String accessToken = jwtService.createAccessToken(accessTokenPayload);
        String refreshToken = jwtService.createRefreshToken(new RefreshTokenPayload(saved.getId().toString(), new Date()));
        ResponseCookie accessTokenCookie = cookieService.createAccessTokenCookie(accessToken);
        ResponseCookie refreshTokenCookie = cookieService.createRefreshTokenCookie(refreshToken);
        return new LoginResponse(user.getRole(), accessTokenCookie, refreshTokenCookie);
    }

    private User getValidatedUser(String email, String password){
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new EmailExistException(ErrorResponseCode.NOT_FOUND, "존재하지 않는 이메일 입니다.");
        }
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new PasswordMatchException(ErrorResponseCode.NOT_MATCH_PASSWORD, "비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

}
