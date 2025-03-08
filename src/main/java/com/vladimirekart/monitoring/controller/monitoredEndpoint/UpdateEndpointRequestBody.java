package com.vladimirekart.monitoring.controller.monitoredEndpoint;

public record UpdateEndpointRequestBody(String name, String path, String service) {}