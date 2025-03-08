package com.vladimirekart.monitoring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.vladimirekart.monitoring.lib.JwtAuthInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
  @Autowired
  private JwtAuthInterceptor jwtAuthInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(jwtAuthInterceptor);
  }
}
