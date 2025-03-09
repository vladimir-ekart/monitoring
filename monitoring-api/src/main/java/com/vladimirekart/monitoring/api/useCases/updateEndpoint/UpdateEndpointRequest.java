package com.vladimirekart.monitoring.api.useCases.updateEndpoint;

public record UpdateEndpointRequest(String name, String path, String service, Integer endpointId) {}