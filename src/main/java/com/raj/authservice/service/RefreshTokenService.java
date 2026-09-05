package com.raj.authservice.service;

import com.raj.authservice.dto.LoginResponse;
import com.raj.authservice.dto.RefreshTokenRequest;
import com.raj.authservice.entity.RefreshToken;
import com.raj.authservice.entity.UserEntity;
import com.raj.authservice.repository.RefreshTokenRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    private final JwtService jwtService;

    private final CustomUserDetailsService customUserDetailsService;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,JwtService jwtService,CustomUserDetailsService customUserDetailsService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    public LoginResponse getToken(RefreshTokenRequest request){

        RefreshToken refreshToken =
                verifyRefreshToken(
                        request.getRefreshToken()
                );

        UserEntity user = refreshToken.getUser();

        RefreshToken newRefreshToken = createRefreshToken(user);

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(user.getEmail());

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        String accessToken =
                jwtService.generateToken(
                        authentication
                );

        LoginResponse response = new LoginResponse();
        response.setRefreshToken(newRefreshToken.getToken());
        response.setAccessToken(accessToken);

        return response;
    }

    public RefreshToken createRefreshToken(UserEntity user) {

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(
                Instant.now().plus(7, ChronoUnit.DAYS)
        );
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyRefreshToken(String token) {

        RefreshToken refreshToken =
                refreshTokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new RuntimeException("Invalid refresh token")
                        );

        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token has expired");
        }

        return refreshToken;
    }
}
