package com.vladimirekart.monitoring.shipper;

import java.util.Date;

public class MonitoringResult {
  private String service;
  private String path;
  private String method;
  private String payload;
  private Date createdAt;
  private Integer statusCode;

  public MonitoringResult(
    String service,
    String path,
    String method,
    String payload,
    Integer statusCode
  ) {
    this.service = service;
    this.path = path;
    this.method = method;
    this.payload = payload;
    this.statusCode = statusCode;
    this.createdAt = new Date();
  }

  public String getService() {
    return service;
  }

  public String getPath() {
    return path;
  }

  public String getMethod() {
    return method;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public String getPayload() {
    return payload;
  }

  public Integer getStatusCode() {
    return statusCode;
  }
}