package com.srinivas.devassist.controller;

import com.srinivas.devassist.model.User;
import com.srinivas.devassist.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/verify-register-otp")
    public String verifyRegister(@RequestBody Map<String,String> data){
        return authService.registerVerifyOtp(
                data.get("username"),
                data.get("otp")
        );
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return authService.login(user);
    }

    @PostMapping("/verify-login-otp")
    public String verifyLogin(@RequestBody Map<String,String> data){
        return authService.loginVerifyOtp(
                data.get("username"),
                data.get("otp")
        );
    }

    @PostMapping("/resend-login-otp")
    public String resendLoginOtp(@RequestBody User user) {
        return authService.resendLoginOtp(user.getUsername());
    }

    @PostMapping("/resend-register-otp")
    public String resendRegisterOtp(@RequestBody User user) {
        return authService.resendRegisterOtp(user.getUsername()); // ✅ FIXED
    }

    @GetMapping("/username")
    public String test(Authentication auth) {
        return "User: " + auth.getName();
    }
}