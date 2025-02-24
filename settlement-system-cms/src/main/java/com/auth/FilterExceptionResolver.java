package com.auth;


import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public interface FilterExceptionResolver<T extends RuntimeException> {
    void setResponse(HttpServletResponse response, T ex) throws IOException;
}