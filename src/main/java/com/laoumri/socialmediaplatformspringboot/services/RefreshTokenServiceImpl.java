package com.laoumri.socialmediaplatformspringboot.services;

import com.laoumri.socialmediaplatformspringboot.entities.RefreshToken;
import com.laoumri.socialmediaplatformspringboot.entities.User;
import com.laoumri.socialmediaplatformspringboot.enums.TokenCode;
import com.laoumri.socialmediaplatformspringboot.exceptions.TokenRefreshException;
import com.laoumri.socialmediaplatformspringboot.exceptions.UserNotFoundException;
import com.laoumri.socialmediaplatformspringboot.repositories.RefreshTokenRepository;
import com.laoumri.socialmediaplatformspringboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Author: Issam Laoumri
 * Email: contact@issamlaoumri.com
 * Date Created: 14/01/2025
 * Version: 0.0.1
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${jwt.refreshExpirationMS}")
    private Long refreshExpirationMS;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserId(userId);

        if (existingToken.isPresent()) {
            return existingToken.get(); // Return existing token instead of a new one
        }

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiresAt(Instant.now().plusMillis(refreshExpirationMS));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request", TokenCode.REFRESH_TOKEN_EXPIRED);
        }

        return token;
    }

    @Transactional
    @Override
    public void deleteByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("user not found with id: "+userId));
        refreshTokenRepository.deleteByUser(user);
    }
}
