package com.laoumri.socialmediaplatformspringboot.security.services;

import com.laoumri.socialmediaplatformspringboot.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService{
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationMs}")
    private int expirationMs;

    @Value("${jwt.cookieName}")
    private String jwtCookie;

    @Override
    public String getJwtTokenFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    @Override
    public String generateJwtTokenFromEmail(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public ResponseCookie generateJwtCookie(User user) {
        String jwt = generateJwtTokenFromEmail(user.getUsername());
        return ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge((long) 24 * 60 * 60).httpOnly(true).build();
    }

    @Override
    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public boolean isTokenValid(String token) {
        return false;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
