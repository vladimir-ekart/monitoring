package com.vladimirekart.monitoring.api.service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vladimirekart.monitoring.api.entity.User;
import com.vladimirekart.monitoring.api.error.UnauthorizedException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class AuthorizerService {
    @Value("${jwt.secret}")
    private String jwtSecret;
    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public User parseToken(String token) {
        Map<String, Object> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        return new User(claims.get("email").toString());
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new UnauthorizedException("User not authorized");
        }
    }
}
