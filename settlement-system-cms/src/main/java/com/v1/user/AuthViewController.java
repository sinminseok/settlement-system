package com.v1.user;


import com.auth.SecurityContextHelper;
import com.domain.user.entity.Role;
import com.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthViewController {

    private final SecurityContextHelper securityContextHelper;
    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login"; // login.html 반환
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @GetMapping("/user-login")
    public String login(){
        return "owner/owner_main";
    }
}

