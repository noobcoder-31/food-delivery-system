package com.raj.authservice.service;

import com.raj.authservice.entity.RefreshToken;
import com.raj.authservice.entity.UserEntity;
import com.raj.authservice.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
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
}
