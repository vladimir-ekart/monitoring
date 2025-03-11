package com.vladimirekart.monitoring.api.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MonitoredEndpoint {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  private String name;
  private String path;
  private String service;
  private Date createdAt;
  private String owner;

  public MonitoredEndpoint() {}
  private MonitoredEndpoint(
    String name,
    String path,
    String service,
    String owner,
    Date createdAt
  ) {
    this.name = name;
    this.path = path;
    this.service = service;
    this.createdAt = createdAt;
    this.owner = owner;
  }

  public static MonitoredEndpoint fromNew(String name, String path, String service, String owner) {
    return new MonitoredEndpoint(name, path, service, owner, new Date());
  }

  public Boolean isOwner(User user) {
    return user.getEmail().equals(owner);
  }

  public void updateBaseInfo(String name, String path, String service) {
    if (name != null) {
      this.name = name;
    }

    if (path != null) {
      this.path = path;
    }

    if (service != null) {
      this.service = service;
    }
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public String getService() {
    return service;
  }

  public String getOwner() {
    return owner;
  }
}
