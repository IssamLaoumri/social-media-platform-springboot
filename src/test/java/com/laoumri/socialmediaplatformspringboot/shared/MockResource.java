package com.laoumri.socialmediaplatformspringboot.shared;

import com.laoumri.socialmediaplatformspringboot.entities.Role;
import com.laoumri.socialmediaplatformspringboot.entities.User;
import com.laoumri.socialmediaplatformspringboot.enums.ERole;

import java.util.Set;

/**
 * Author: Issam Laoumri
 * Email: contact@issamlaoumri.com
 * Date Created: 05/01/2025
 * Version: 0.0.1
 */
public class MockResource {
    public static User getMockUserIssam(){
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
                .build();
    }
    public static Role getRoleUser(){
        return new Role(null, ERole.ROLE_USER);
    }
    public static Role getRoleAdmin(){
        return new Role(null, ERole.ROLE_ADMIN);
    }
}
