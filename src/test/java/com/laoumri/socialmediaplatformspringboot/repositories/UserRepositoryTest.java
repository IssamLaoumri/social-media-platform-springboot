package com.laoumri.socialmediaplatformspringboot.repositories;

import com.laoumri.socialmediaplatformspringboot.entities.User;
import com.laoumri.socialmediaplatformspringboot.shared.MockResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: Issam Laoumri
 * Email: contact@issamlaoumri.com
 * Date Created: 05/01/2025
 * Version: 0.0.1
 */

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    private final User USER_ISSAM = MockResource.getMockUser();

    @BeforeEach
    void setup(){
        userRepository.save(USER_ISSAM);
    }

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnOptionalUser_whenEmailIsGiven(){
        Optional<User> returnedUser = userRepository.findByUsername(USER_ISSAM.getUsername());
        assertThat(returnedUser).isPresent();
    }

    @Test
    void shouldReturnTrue_whenEmailIsGiven(){
        assertThat(userRepository.existsByUsername(USER_ISSAM.getUsername())).isTrue();
    }

}
