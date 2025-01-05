package com.laoumri.socialmediaplatformspringboot.services;

import com.laoumri.socialmediaplatformspringboot.dto.requests.SignupRequest;
import com.laoumri.socialmediaplatformspringboot.entities.Role;
import com.laoumri.socialmediaplatformspringboot.entities.User;
import com.laoumri.socialmediaplatformspringboot.enums.ERole;
import com.laoumri.socialmediaplatformspringboot.exceptions.EmailALreadyExistsException;
import com.laoumri.socialmediaplatformspringboot.repositories.RoleRepository;
import com.laoumri.socialmediaplatformspringboot.repositories.UserRepository;
import com.laoumri.socialmediaplatformspringboot.shared.MockResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Author: Issam Laoumri
 * Email: contact@issamlaoumri.com
 * Date Created: 05/01/2025
 * Version: 0.0.1
 */

@DataJpaTest
public class UserServiceTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    private static User USER_ISSAM = MockResource.getMockUserIssam();

    @BeforeEach
    void setUp() {}

    @AfterEach
    void tearDown() {}

    @Test
    void shouldReturnUser_whenUserEmailIsGiven(){
        when(userRepository.findByUsername(USER_ISSAM.getUsername())).thenReturn(Optional.of(USER_ISSAM));
        User returnedUser = userService.getUserByEmail(USER_ISSAM.getUsername());

        assertThat(returnedUser).isNotNull();
        assertThat(returnedUser).isEqualTo(USER_ISSAM);
    }

    @Test
    void shouldCreateNewUser_whenSignupRequestIsGiven() {
        SignupRequest request = SignupRequest.builder()
                .firstname(USER_ISSAM.getFirstname())
                .lastname(USER_ISSAM.getLastname())
                .email(USER_ISSAM.getUsername())
                .password(USER_ISSAM.getPassword())
                .bDay(USER_ISSAM.getBDay())
                .bMonth(USER_ISSAM.getBMonth())
                .bYear(USER_ISSAM.getBYear())
                .gender(USER_ISSAM.getGender())
                .build();
        Role userRole = new Role(null, ERole.ROLE_USER);

        when(userRepository.existsByUsername(USER_ISSAM.getUsername())).thenReturn(false);
        when(roleRepository.findByName(any())).thenReturn(Optional.of(userRole));
        when(userRepository.save(any(User.class))).thenReturn(USER_ISSAM);

        User savedUser = userService.createNewUser(request);

        verify(userRepository).save(any(User.class));

        assertThat(savedUser).isNotNull();
    }
}
