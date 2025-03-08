package com.vladimirekart.monitoring.entity;

public class User {
  private final String email;

  public User(
    final String email
  ) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }
}
