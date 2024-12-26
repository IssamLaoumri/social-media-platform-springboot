package com.laoumri.socialmediaplatformspringboot.controllers;

import com.laoumri.socialmediaplatformspringboot.Services.UserService;
import com.laoumri.socialmediaplatformspringboot.dto.requests.SigninRequest;
import com.laoumri.socialmediaplatformspringboot.dto.requests.SignupRequest;
import com.laoumri.socialmediaplatformspringboot.dto.responses.AuthResponse;
import com.laoumri.socialmediaplatformspringboot.dto.responses.MessageResponse;
import com.laoumri.socialmediaplatformspringboot.entities.User;
import com.laoumri.socialmediaplatformspringboot.enums.InfoCode;
import com.laoumri.socialmediaplatformspringboot.security.services.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@RequestBody @Valid SignupRequest request) {
        User user = userService.createNewUser(request);
        SigninRequest signinRequest = new SigninRequest();
        signinRequest.setEmail(user.getEmail());
        signinRequest.setPassword(request.getPassword());
        return signin(signinRequest); // Intentional for auto-login
    }

    @PostMapping("/signin")
    public ResponseEntity<MessageResponse> signin(@RequestBody @Valid SigninRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User loggedInUser = userService.getUserByEmail(request.getEmail());
        ResponseCookie cookie = jwtService.generateJwtCookie(loggedInUser);

        List<String> roles = loggedInUser.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .toList();

        AuthResponse response = AuthResponse.builder()
                .firstname(loggedInUser.getFirstname())
                .lastname(loggedInUser.getLastname())
                .email(loggedInUser.getEmail())
                .bYear(loggedInUser.getBYear())
                .bMonth(loggedInUser.getBMonth())
                .bDay(loggedInUser.getBDay())
                .gender(loggedInUser.getGender())
                .roles(roles)
                .build();

        MessageResponse message = new MessageResponse(InfoCode.LOGIN_SUCCESS, Instant.now(), response);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(message);
    }
}
