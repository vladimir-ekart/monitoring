package com.vladimirekart.monitoring.api.useCases.addEndpoint;

public record AddEndpointRequest(String name, String path, String service) {}