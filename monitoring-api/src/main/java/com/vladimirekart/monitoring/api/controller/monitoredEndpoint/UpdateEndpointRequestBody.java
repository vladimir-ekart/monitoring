package com.vladimirekart.monitoring.api.controller.monitoredEndpoint;

public record UpdateEndpointRequestBody(String name, String path, String service) {}