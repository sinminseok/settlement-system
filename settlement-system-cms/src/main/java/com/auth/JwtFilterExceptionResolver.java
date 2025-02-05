package com.auth;

import java.io.IOException;

import com.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilterExceptionResolver implements FilterExceptionResolver<JwtException> {

    @Override
    public void setResponse(HttpServletResponse response, JwtException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter()
                .write(new ObjectMapper().writeValueAsString(
                        new ErrorResponse(HttpStatus.UNAUTHORIZED.name(), ex.getMessage())));

    }
}