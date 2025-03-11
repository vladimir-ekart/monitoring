package com.vladimirekart.monitoring.api.useCases.deleteEndpoint;

import jakarta.validation.constraints.NotBlank;

public record DeleteEndpointRequest(
  @NotBlank(message = "Endpoint ID is required")
  Integer endpointId
) {}