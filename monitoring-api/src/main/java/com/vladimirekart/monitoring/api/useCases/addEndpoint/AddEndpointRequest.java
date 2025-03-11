package com.vladimirekart.monitoring.api.useCases.addEndpoint;

import jakarta.validation.constraints.NotNull;

public record AddEndpointRequest(
  @NotNull(message = "Name is required")
  String name, 

  @NotNull(message = "Path is required")
  String path,

  @NotNull(message = "Service is required")
  String service
) {}