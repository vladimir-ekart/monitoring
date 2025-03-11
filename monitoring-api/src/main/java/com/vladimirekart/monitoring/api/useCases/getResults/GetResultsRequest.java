package com.vladimirekart.monitoring.api.useCases.getResults;

import jakarta.validation.constraints.NotBlank;

public record GetResultsRequest(
  @NotBlank(message = "Endpoint ID is required")
  Integer endpointId
) {}