package com.auth.jwt;


import com.domain.user.entity.Role;

import java.util.Date;


public record AccessTokenPayload(String email, Role roleEnum, Date issuedAt) {
}