package com.exception;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class LoginException extends ExceptionBase{

    public LoginException(ErrorResponseCode errorResponseCode, @Nullable String message) {
        this.errorCode = errorResponseCode;
        this.errorMessage = message;
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}
