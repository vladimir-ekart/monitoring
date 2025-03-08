package com.vladimirekart.monitoring.useCases.updateEndpoint;

public record UpdateEndpointRequest(String name, String path, String service, Integer endpointId) {}