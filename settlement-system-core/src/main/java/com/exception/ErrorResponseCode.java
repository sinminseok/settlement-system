package com.exception;

public enum ErrorResponseCode {
    NOT_VALID_TOKEN(4011),
    FAIL_SEND_TOKEN(4012),
    FAIL_LOGIN(4013),
    NOT_FOUND(4041),
    INVALID_DATA(4042),
    NOT_MATCH_PASSWORD(5001);

    private final int code;

    ErrorResponseCode(int c) {
        this.code = c;
    }

    public int getCode() {
        return this.code;
    }
}