package com.vladimirekart.monitoring.api.lib;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.github.javafaker.Faker;
import com.vladimirekart.monitoring.api.entity.User;
import com.vladimirekart.monitoring.api.error.UnauthorizedException;
import com.vladimirekart.monitoring.api.service.AuthorizerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JwtAuthInterceptorUnitTest {

  private JwtAuthInterceptor interceptor;
  private AuthorizerService authorizerService;
  private HttpServletRequest request;
  private HttpServletResponse response;

  private Faker faker = new Faker();

  @BeforeEach
  void setUp() {
    authorizerService = mock(AuthorizerService.class);
    interceptor = new JwtAuthInterceptor();
    interceptor.authorizerService = authorizerService;

    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
  }

  @Test
  void shouldPassWithValidToken() {
    String token = "valid.jwt.token";
    User user = new User(faker.internet().emailAddress());

    when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
    when(authorizerService.validateJwtToken(token)).thenReturn(true);
    when(authorizerService.parseToken(token)).thenReturn(user);

    boolean result = interceptor.preHandle(request, response, new Object());

    assertTrue(result);
    verify(request).setAttribute("user", user);
  }

  @Test
  void shouldFailWhenAuthorizationHeaderIsMissing() {
    when(request.getHeader("Authorization")).thenReturn(null);

    assertThrows(UnauthorizedException.class, () -> interceptor.preHandle(request, response, new Object()));
  }

  @Test
  void shouldFailWhenAuthorizationHeaderIsInvalidFormat() {
    when(request.getHeader("Authorization")).thenReturn("InvalidFormat token");

    assertThrows(UnauthorizedException.class, () -> interceptor.preHandle(request, response, new Object()));
  }

  @Test
  void shouldFailWhenTokenIsInvalid() {
    String token = "invalid.jwt.token";

    when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
    doThrow(new UnauthorizedException("User not authorized")).when(authorizerService).validateJwtToken(token);

    assertThrows(UnauthorizedException.class, () -> interceptor.preHandle(request, response, new Object()));
  }
}
