package com.vladimirekart.monitoring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

import com.vladimirekart.monitoring.lib.MonitoringResponseFilter;

@Configuration
public class FilterConfig {
  @Autowired
  private MonitoringResponseFilter monitoringResponseFilter;

  public FilterRegistrationBean<MonitoringResponseFilter> responseCachingFilter() {
        FilterRegistrationBean<MonitoringResponseFilter> registrationBean = new FilterRegistrationBean<>();
        
        registrationBean.setFilter(monitoringResponseFilter);
        registrationBean.addUrlPatterns("/*"); // Apply to all requests

        return registrationBean;
    }
}
