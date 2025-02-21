package com.generator;

import com.domain.user.entity.Role;
import com.domain.user.entity.User;

public class UserHelper {

    public static User createUser(){
        return User.builder()
                .role(Role.OWNER)
                .email("test12@test.com")
                .password("password")
                .build();
    }
}
