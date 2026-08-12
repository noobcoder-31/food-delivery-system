package com.raj.authservice.controller;

import com.raj.authservice.dto.*;
import com.raj.authservice.entity.UserEntity;
import com.raj.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    UserDetailsService userDetailsService;


    @PostMapping("/register")
    public RegisterResponse registerUser(@Valid @RequestBody RegisterRequest request){
        return authService.registerUser(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request){
        return authService.loginUser(request);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDataResponse> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();

        UserDetails user = userDetailsService.loadUserByUsername(email);
        String role = user.getAuthorities()
                .iterator()
                .next()
                .getAuthority();
        UserDataResponse userDataResponse = new UserDataResponse(user.getUsername(),role);
        return ResponseEntity.ok(userDataResponse);
    }

}
