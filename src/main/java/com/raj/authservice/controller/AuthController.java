package com.raj.authservice.controller;

import com.raj.authservice.dto.LoginRequest;
import com.raj.authservice.dto.LoginResponse;
import com.raj.authservice.dto.RegisterRequest;
import com.raj.authservice.dto.RegisterResponse;
import com.raj.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;


    @PostMapping("/register")
    public RegisterResponse registerUser(@Valid @RequestBody RegisterRequest request){
        return authService.registerUser(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request){
        return authService.loginUser(request);
    }

}
