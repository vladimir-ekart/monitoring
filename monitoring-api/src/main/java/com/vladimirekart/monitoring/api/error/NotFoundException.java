package com.vladimirekart.monitoring.api.error;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
      super(message);
  }
}
