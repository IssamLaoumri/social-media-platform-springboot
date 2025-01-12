package com.laoumri.socialmediaplatformspringboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laoumri.socialmediaplatformspringboot.dto.requests.SigninRequest;
import com.laoumri.socialmediaplatformspringboot.dto.requests.SignupRequest;
import com.laoumri.socialmediaplatformspringboot.entities.Role;
import com.laoumri.socialmediaplatformspringboot.entities.User;
import com.laoumri.socialmediaplatformspringboot.enums.EGender;
import com.laoumri.socialmediaplatformspringboot.repositories.RoleRepository;
import com.laoumri.socialmediaplatformspringboot.repositories.UserRepository;
import com.laoumri.socialmediaplatformspringboot.shared.MockResource;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Author: Issam Laoumri
 * Email: contact@issamlaoumri.com
 * Date Created: 05/01/2025
 * Version: 0.0.1
 */

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {
    @Value("${jwt.cookieName}")
    private String cookieName;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper mapper = new ObjectMapper();
    private final String API_URL_PREFIX = "/api/v1/auth";

    private final User userIssam = MockResource.getMockUserIssam();

    @BeforeEach
    void setup() {
        roleRepository.deleteAll();
        userRepository.deleteAll();

        Role roleUser = roleRepository.save(MockResource.getRoleUser());
        Role roleAdmin = roleRepository.save(MockResource.getRoleAdmin());

        userIssam.setPassword(passwordEncoder.encode(userIssam.getPassword()));
        userIssam.setRoles(Set.of(roleUser,roleAdmin));
        userRepository.save(userIssam);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void signup() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .firstname("issam")
                .lastname("laoumri")
                .email("issam@issamlaoumri.com")
                .password("I123!abc@cBa")
                .bDay(1)
                .bMonth(2)
                .bYear(1990)
                .gender(EGender.MALE)
                .build();
        String requestJson = mapper.writeValueAsString(request);

        mockMvc.perform(post(API_URL_PREFIX + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("issam@issamlaoumri.com"));
    }

    @Test
    void shouldReturnValidationErrors() throws Exception {
        SignupRequest request = SignupRequest.builder()
                .firstname("i")
                .lastname("l")
                .email("issamlaoumri.com")
                .password("I123!abc@cBa")
                .bDay(1)
                .bMonth(2)
                .bYear(1990)
                .gender(EGender.MALE)
                .build();
        String requestJson = mapper.writeValueAsString(request);
        mockMvc.perform(post(API_URL_PREFIX + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.message.length()").value(Matchers.greaterThanOrEqualTo(3)));
    }

    @Test
    void signin() throws Exception {
        SigninRequest request = SigninRequest.builder()
                .email(userIssam.getUsername())
                .password("I123!abc@cBa")
                .build();
        String requestJson = mapper.writeValueAsString(request);

        mockMvc.perform(post(API_URL_PREFIX + "/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(userIssam.getUsername()))
                .andExpect(cookie().exists(cookieName));
    }
}
