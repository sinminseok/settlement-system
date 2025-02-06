package com.exception;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class PasswordMatchException extends ExceptionBase {

    public PasswordMatchException(ErrorResponseCode errorResponseCode, @Nullable String message) {
        this.errorCode = errorResponseCode;
        this.errorMessage = message;
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}

