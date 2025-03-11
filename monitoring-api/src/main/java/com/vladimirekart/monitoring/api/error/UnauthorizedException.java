package com.vladimirekart.monitoring.api.error;

public class UnauthorizedException extends RuntimeException {
  public UnauthorizedException(String message) {
      super(message);
  }
}
