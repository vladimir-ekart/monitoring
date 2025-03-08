package com.vladimirekart.monitoring.lib;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.vladimirekart.monitoring.entity.MonitoringResult;
import com.vladimirekart.monitoring.repository.MonitoringResultRepository;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MonitoringResponseFilter implements Filter {
  @Value("${service.name}")
  private String service;

  @Autowired
  private MonitoringResultRepository monitoringResultRepository;

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    ContentCachingResponseWrapper responseCacheWrapperObject = new ContentCachingResponseWrapper(
        (HttpServletResponse) servletResponse);

    filterChain.doFilter(servletRequest, responseCacheWrapperObject);
    byte[] body = responseCacheWrapperObject.getContentAsByteArray();

    if (servletRequest instanceof HttpServletRequest) {
      HttpServletRequest httpRequest = ((HttpServletRequest) servletRequest);
      String path = httpRequest.getRequestURI();
      String method = httpRequest.getMethod();

      MonitoringResult monitoringResult = MonitoringResult.fromNew(service, path, method, new String(body), responseCacheWrapperObject.getStatus());
      monitoringResultRepository.save(monitoringResult);
    }

    responseCacheWrapperObject.copyBodyToResponse();
  }
}
