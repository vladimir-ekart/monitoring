package com.vladimirekart.monitoring.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.vladimirekart.monitoring.shipper.ResponseFilter;

@Configuration
@ComponentScan(basePackages = "com.vladimirekart.monitoring.shipper")
public class FilterConfig {
  @Autowired
  private ResponseFilter responseFilter;

  public FilterRegistrationBean<ResponseFilter> responseCachingFilter() {
        FilterRegistrationBean<ResponseFilter> registrationBean = new FilterRegistrationBean<>();
        
        registrationBean.setFilter(responseFilter);
        registrationBean.addUrlPatterns("/*"); // Apply to all requests

        return registrationBean;
    }
}
