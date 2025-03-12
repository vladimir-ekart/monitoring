package com.vladimirekart.monitoring.api.service;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;
import com.vladimirekart.monitoring.api.entity.User;
import com.vladimirekart.monitoring.api.error.UnauthorizedException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class AuthorizerServiceTest {

  private AuthorizerService authorizerService;
  private String jwtSecret = "authorizer-service-test-secret-key-jwt";
  private SecretKey key;

  private Faker faker = new Faker();

  @BeforeEach
  void setUp() {
    authorizerService = new AuthorizerService();
    authorizerService.jwtSecret = jwtSecret;
    authorizerService.init();

    key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }

  private String generateToken(Map<String, Object> claims, Date expiration) {
    return Jwts.builder()
        .setClaims(claims)
        .setExpiration(expiration)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  @Test
  void shouldParseTokenAndReturnUser() {
    String email = faker.internet().emailAddress();
    String token = generateToken(Map.of("email", email), new Date(System.currentTimeMillis() + 100000));

    User user = authorizerService.parseToken(token);

    assertEquals(email, user.getEmail());
  }

  @Test
  void shouldValidateCorrectToken() {
    String token = generateToken(Map.of("email", faker.internet().emailAddress()),
        new Date(System.currentTimeMillis() + 100000));

    boolean isValid = authorizerService.validateJwtToken(token);

    assertTrue(isValid);
  }

  @Test
  void shouldThrowExceptionForInvalidToken() {
    String invalidToken = "invalid.jwt.token";

    assertThrows(UnauthorizedException.class, () -> authorizerService.validateJwtToken(invalidToken));
  }

  @Test
  void shouldThrowExceptionForExpiredToken() {
    String token = generateToken(Map.of("email", faker.internet().emailAddress()),
        new Date(System.currentTimeMillis() - 100000));

    assertThrows(UnauthorizedException.class, () -> authorizerService.validateJwtToken(token));
  }
}
