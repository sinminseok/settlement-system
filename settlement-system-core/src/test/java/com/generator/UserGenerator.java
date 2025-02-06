package com.generator;


import com.domain.user.entity.Role;
import com.domain.user.entity.User;

public class UserGenerator {

    public static User generateUser(){
        return User.builder()
                .email("testUser@test.com")
                .password("password")
                .role(Role.ADMIN)
                .build();
    }
}
