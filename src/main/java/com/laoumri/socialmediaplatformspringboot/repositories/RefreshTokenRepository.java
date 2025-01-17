package com.laoumri.socialmediaplatformspringboot.repositories;

import com.laoumri.socialmediaplatformspringboot.entities.RefreshToken;
import com.laoumri.socialmediaplatformspringboot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String token);
    Optional<RefreshToken> findByUserId(Long userId);
    @Modifying
    void deleteByUser(User user);
}
