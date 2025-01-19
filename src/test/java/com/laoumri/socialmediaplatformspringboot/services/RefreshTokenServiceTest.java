package com.laoumri.socialmediaplatformspringboot.services;

import com.laoumri.socialmediaplatformspringboot.entities.RefreshToken;
import com.laoumri.socialmediaplatformspringboot.entities.User;
import com.laoumri.socialmediaplatformspringboot.exceptions.TokenRefreshException;
import com.laoumri.socialmediaplatformspringboot.exceptions.UserNotFoundException;
import com.laoumri.socialmediaplatformspringboot.repositories.RefreshTokenRepository;
import com.laoumri.socialmediaplatformspringboot.repositories.UserRepository;
import com.laoumri.socialmediaplatformspringboot.shared.MockResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Author: Issam Laoumri
 * Email: contact@issamlaoumri.com
 * Date Created: 19/01/2025
 * Version: 0.0.1
 */
@DataJpaTest
class RefreshTokenServiceTest {

    @InjectMocks
    RefreshTokenServiceImpl refreshTokenService;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Mock
    UserRepository userRepository;

    private static final User MOCK_USER = MockResource.getMockUser();
    private static final RefreshToken MOCK_REFRESH_TOKEN = MockResource.getMockRefreshToken(MOCK_USER);

    @BeforeAll
    static void init() {
        MOCK_REFRESH_TOKEN.setUser(MOCK_USER);
    }

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        // Manually inject refreshExpirationMS since @Value won't populate it
        Field expirationField = RefreshTokenServiceImpl.class.getDeclaredField("refreshExpirationMS");
        expirationField.setAccessible(true);
        expirationField.set(refreshTokenService, 3600000L);
    }

    @AfterEach
    void tearDown() {}

    @Test
    void shouldFindToken_whenTokenIsGiven() {
        when(refreshTokenRepository.findByToken(MOCK_REFRESH_TOKEN.getToken()))
                .thenReturn(Optional.of(MOCK_REFRESH_TOKEN));

        Optional<RefreshToken> foundToken = refreshTokenService.findByToken(MOCK_REFRESH_TOKEN.getToken());

        assertThat(foundToken).isPresent();
        assertThat(foundToken.get()).isEqualTo(MOCK_REFRESH_TOKEN);
    }

    @Test
    void shouldCreateNewRefreshToken_whenNoExistingTokenForUser() {
        when(refreshTokenRepository.findByUserId(MOCK_USER.getId())).thenReturn(Optional.empty());
        when(userRepository.findById(MOCK_USER.getId())).thenReturn(Optional.of(MOCK_USER));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(MOCK_REFRESH_TOKEN);

        RefreshToken createdToken = refreshTokenService.createRefreshToken(MOCK_USER.getId());

        assertThat(createdToken).isNotNull();
        assertThat(createdToken.getUser()).isEqualTo(MOCK_USER);
        assertThat(createdToken.getToken()).isNotBlank();
    }

    @Test
    void shouldReturnExistingToken_whenTokenExistsForUser() {
        when(refreshTokenRepository.findByUserId(MOCK_USER.getId())).thenReturn(Optional.of(MOCK_REFRESH_TOKEN));

        RefreshToken returnedToken = refreshTokenService.createRefreshToken(MOCK_USER.getId());

        assertThat(returnedToken).isEqualTo(MOCK_REFRESH_TOKEN);
        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }

    @Test
    void shouldVerifyExpiration_whenTokenIsValid() {
        when(refreshTokenRepository.findByToken(MOCK_REFRESH_TOKEN.getToken()))
                .thenReturn(Optional.of(MOCK_REFRESH_TOKEN));

        RefreshToken verifiedToken = refreshTokenService.verifyExpiration(MOCK_REFRESH_TOKEN);

        assertThat(verifiedToken).isEqualTo(MOCK_REFRESH_TOKEN);
    }

    @Test
    void shouldThrowException_whenTokenIsExpired() {
        RefreshToken expiredToken = MOCK_REFRESH_TOKEN;
        expiredToken.setExpiresAt(Instant.now().minusSeconds(1));

        when(refreshTokenRepository.findByToken(expiredToken.getToken()))
                .thenReturn(Optional.of(expiredToken));

        assertThrows(TokenRefreshException.class, () -> refreshTokenService.verifyExpiration(expiredToken));
        verify(refreshTokenRepository).delete(expiredToken);
    }

    @Test
    void shouldDeleteTokensByUserId_whenUserExists() {
        when(userRepository.findById(MOCK_USER.getId())).thenReturn(Optional.of(MOCK_USER));

        refreshTokenService.deleteByUserId(MOCK_USER.getId());

        verify(refreshTokenRepository).deleteByUser(MOCK_USER);
    }

    @Test
    void shouldThrowException_whenDeletingTokenForNonExistentUser() {
        when(userRepository.findById(MOCK_USER.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> refreshTokenService.deleteByUserId(MOCK_USER.getId()));
        verify(refreshTokenRepository, never()).deleteByUser(any(User.class));
    }
}

