package com.vladimirekart.monitoring.api.error;

public class ForbiddenException extends RuntimeException {
  public ForbiddenException(String message) {
      super(message);
  }
}
