package com.vladimirekart.monitoring.api.entity;

import java.util.Date;

import jakarta.persistence.Column;
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
  
  @Column(length = 10000)
  private String payload;

  private String method;
  private Date createdAt;
  private Integer statusCode;

  public MonitoringResult() {}
  private MonitoringResult(
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

  public static MonitoringResult fromNew(
    String service,
    String path,
    String method,
    String payload,
    Integer statusCode
  ) {
    return new MonitoringResult(service, path, method, payload, statusCode);
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
