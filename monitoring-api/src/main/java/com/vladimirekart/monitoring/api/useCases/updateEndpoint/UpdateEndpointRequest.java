package com.vladimirekart.monitoring.api.useCases.updateEndpoint;

import jakarta.validation.constraints.NotNull;

public record UpdateEndpointRequest(
  String name,
  String path,
  String service,

  @NotNull(message = "Endpoint ID is required")
  Integer endpointId
) {}