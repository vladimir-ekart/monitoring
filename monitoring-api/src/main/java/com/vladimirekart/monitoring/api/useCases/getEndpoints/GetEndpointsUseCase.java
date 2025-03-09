package com.vladimirekart.monitoring.api.useCases.getEndpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vladimirekart.monitoring.api.entity.MonitoredEndpoint;
import com.vladimirekart.monitoring.api.entity.User;
import com.vladimirekart.monitoring.api.repository.MonitoredEndpointRepository;
import com.vladimirekart.monitoring.api.useCases.UseCase;

@Service
public class GetEndpointsUseCase implements UseCase<GetEndpointsRequest, Iterable<MonitoredEndpoint>> {
  @Autowired
  private MonitoredEndpointRepository monitoredEndpointRepository;

  @Override
  public Iterable<MonitoredEndpoint> run(GetEndpointsRequest request, User user) {
    Iterable<MonitoredEndpoint> endpoints = monitoredEndpointRepository.findByOwner(user.getEmail());

    return endpoints;
  }
}
