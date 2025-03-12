package com.vladimirekart.monitoring.collector;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MonitoringResult {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String service;
  private String path;

  private String payload;

  private String method;
  private Date createdAt;
  private Integer statusCode;

  public MonitoringResult() {}

  public MonitoringResult(
      String service,
      String path,
      String payload,
      String method,
      Date createdAt,
      Integer statusCode
  ) {
    this.service = service;
    this.path = path;
    this.payload = payload;
    this.method = method;
    this.createdAt = createdAt;
    this.statusCode = statusCode;
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

  public Integer getId() {
    return id;
  }
}