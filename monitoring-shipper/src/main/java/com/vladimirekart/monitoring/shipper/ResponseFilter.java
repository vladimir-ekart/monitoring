package com.vladimirekart.monitoring.shipper;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ResponseFilter implements Filter {
  @Value("${monitoring.service.name}")
  private String service;

  @Value("${monitoring.collector.url}")
  private String collectorUrl;

  private final RestTemplate restTemplate = new RestTemplate();

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

      MonitoringResult monitoringResult = new MonitoringResult(service, path, method, new String(body), responseCacheWrapperObject.getStatus());

      sendMonitoringResult(monitoringResult);
    }

    responseCacheWrapperObject.copyBodyToResponse();
  }

  private void sendMonitoringResult(MonitoringResult monitoringResult) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<MonitoringResult> request = new HttpEntity<>(monitoringResult, headers);

      restTemplate.postForEntity(collectorUrl, request, String.class);
    } catch (Exception e) {
      System.err.println("Failed to send monitoring data: " + e.getMessage());
    }
  }
}

