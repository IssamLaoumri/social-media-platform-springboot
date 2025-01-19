package com.laoumri.socialmediaplatformspringboot.shared;

import com.laoumri.socialmediaplatformspringboot.entities.RefreshToken;
import com.laoumri.socialmediaplatformspringboot.entities.Role;
import com.laoumri.socialmediaplatformspringboot.entities.User;
import com.laoumri.socialmediaplatformspringboot.enums.EGender;
import com.laoumri.socialmediaplatformspringboot.enums.ERole;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Author: Issam Laoumri
 * Email: contact@issamlaoumri.com
 * Date Created: 05/01/2025
 * Version: 0.0.1
 */
public class MockResource {
    public static User getMockUser(){
        return User.builder()
                .firstname("issam")
                .lastname("laoumri")
                .username("contact@issamlaoumri.com")
                .password("I123!abc@cBa")
                .bDay(1)
                .bMonth(2)
                .bYear(1990)
                .userIdentifier("issam.laoumri")
                .roles(Set.of(getRoleUser()))
                .gender(EGender.MALE)
                .build();
    }
    public static RefreshToken getMockRefreshToken(User user){
        return RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiresAt(Instant.now().plusMillis(180000))
                .user(user)
                .id(1L)
                .build();
    }
    public static RefreshToken getMockExpiredRefreshToken(User user){
        return RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiresAt(Instant.now().minusMillis(180000))
                .user(user)
                .id(2L)
                .build();
    }
    public static Role getRoleUser(){
        return new Role(null, ERole.ROLE_USER);
    }
    public static Role getRoleAdmin(){
        return new Role(null, ERole.ROLE_ADMIN);
    }
}
