package com.vladimirekart.monitoring.useCases.addEndpoint;

public record AddEndpointRequest(String name, String path, String service) {}