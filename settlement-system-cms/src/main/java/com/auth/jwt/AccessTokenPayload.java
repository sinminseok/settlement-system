package com.auth.jwt;


import com.domain.user.entity.RoleEnum;

import java.util.Date;


public record AccessTokenPayload(String email, RoleEnum roleEnum, Date issuedAt) {

}