package com.laoumri.socialmediaplatformspringboot.controllers;

import com.laoumri.socialmediaplatformspringboot.dto.requests.SigninRequest;
import com.laoumri.socialmediaplatformspringboot.dto.requests.SignupRequest;
import com.laoumri.socialmediaplatformspringboot.dto.responses.AuthResponse;
import com.laoumri.socialmediaplatformspringboot.dto.responses.MessageResponse;
import com.laoumri.socialmediaplatformspringboot.entities.RefreshToken;
import com.laoumri.socialmediaplatformspringboot.entities.User;
import com.laoumri.socialmediaplatformspringboot.enums.InfoCode;
import com.laoumri.socialmediaplatformspringboot.exceptions.TokenRefreshException;
import com.laoumri.socialmediaplatformspringboot.security.services.JwtService;
import com.laoumri.socialmediaplatformspringboot.services.RefreshTokenService;
import com.laoumri.socialmediaplatformspringboot.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@RequestBody @Valid SignupRequest request) {
        User user = userService.createNewUser(request);
        SigninRequest signinRequest = new SigninRequest();
        signinRequest.setEmail(user.getUsername());
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
        ResponseCookie jwtCookie = jwtService.generateJwtCookie(loggedInUser);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loggedInUser.getId());
        ResponseCookie jwtRefreshToken = jwtService.generateJwtRefreshCookie(refreshToken.getRefreshToken());

        List<String> roles = loggedInUser.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .toList();

        AuthResponse response = AuthResponse.builder()
                .firstname(loggedInUser.getFirstname())
                .lastname(loggedInUser.getLastname())
                .email(loggedInUser.getUsername())
                .bYear(loggedInUser.getBYear())
                .bMonth(loggedInUser.getBMonth())
                .bDay(loggedInUser.getBDay())
                .gender(loggedInUser.getGender())
                .roles(roles)
                .build();

        MessageResponse message = new MessageResponse(InfoCode.LOGIN_SUCCESS, Instant.now(), response);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshToken.toString())
                .body(message);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principle.toString() != "anonymousUser") {
            Long userId = ((User) principle).getId();
            refreshTokenService.deleteByUserId(userId);
        }

        ResponseCookie jwtCookie = jwtService.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtService.getCleanJwtRefreshCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .body(new MessageResponse(InfoCode.SIGNED_OUT, Instant.now(), "You've been signed out!"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
        String refreshToken = jwtService.getJwtRefreshFromCookies(request);

        if ((refreshToken != null) && (!refreshToken.isEmpty())) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        ResponseCookie jwtCookie = jwtService.generateJwtCookie(user);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new MessageResponse(InfoCode.TOKEN_REFRESH_SUCCESS, Instant.now(), "Token is refreshed successfully!"));
                    })
                    .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in database!"));
        }

        return ResponseEntity.badRequest().body(new MessageResponse(InfoCode.TOKEN_REFRESH_FAIL, Instant.now(), "Refresh Token is empty!"));
    }

}
