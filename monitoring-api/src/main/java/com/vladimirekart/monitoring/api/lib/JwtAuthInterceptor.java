package com.vladimirekart.monitoring.api.lib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.vladimirekart.monitoring.api.entity.User;
import com.vladimirekart.monitoring.api.service.AuthorizerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {
  @Autowired
  private AuthorizerService authorizerService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    
    if (authHeader == null || !authHeader.startsWith("Bearer ")) throw new Exception("Invalid or missing auth");

    String token = authHeader.substring(7);

    authorizerService.validateJwtToken(token);
    User user = authorizerService.parseToken(token);

    request.setAttribute("user", user);

    return true;
  }
}
