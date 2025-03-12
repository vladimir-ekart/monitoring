package com.vladimirekart.monitoring.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.vladimirekart.monitoring.api.lib.JwtAuthInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@ActiveProfiles("test")
public class TestInterceptorConfig implements WebMvcConfigurer {

  @Bean
  @Primary
  public JwtAuthInterceptor jwtAuthInterceptor() {
    return new JwtAuthInterceptor() {
      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
      }
    };
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(jwtAuthInterceptor());
  }
}
