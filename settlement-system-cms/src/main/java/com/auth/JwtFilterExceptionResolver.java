package com.auth;

import java.io.IOException;

import com.exception.AuthException;
import com.exception.ErrorResponseCode;
import com.v1.response.SuccessResponse;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilterExceptionResolver implements FilterExceptionResolver<JwtException> {

    @Override
    public void setResponse(HttpServletResponse response, JwtException ex) throws IOException {
        AuthException exception = new AuthException(ErrorResponseCode.NOT_VALID_TOKEN, "InValid Jwt Token");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new SuccessResponse(false, exception.getErrorMessage(), exception.getErrorCode().getCode())));
    }
}