package com.raj.authservice.controller;

import com.raj.authservice.dto.*;
import com.raj.authservice.entity.RefreshToken;
import com.raj.authservice.entity.UserEntity;
import com.raj.authservice.service.AuthService;
import com.raj.authservice.service.CustomUserDetailsService;
import com.raj.authservice.service.JwtService;
import com.raj.authservice.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomUserDetailsService customUserDetailsService;


    @PostMapping("/register")
    public RegisterResponse registerUser(@Valid @RequestBody RegisterRequest request){
        return authService.registerUser(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request){
        return authService.loginUser(request);
    }


    @GetMapping("/me")
//    @PreAuthorize("hasRole('ADMIN')")
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

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(
            @RequestBody RefreshTokenRequest request) {

        LoginResponse response = refreshTokenService.getToken(request);

        return ResponseEntity.ok(
                response
        );
    }

}
