package com.vladimirekart.monitoring.api.useCases.addEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vladimirekart.monitoring.api.entity.MonitoredEndpoint;
import com.vladimirekart.monitoring.api.entity.User;
import com.vladimirekart.monitoring.api.repository.MonitoredEndpointRepository;
import com.vladimirekart.monitoring.api.useCases.UseCase;

@Service
public class AddEndpointUseCase implements UseCase<AddEndpointRequest, MonitoredEndpoint> {
  @Autowired
  private MonitoredEndpointRepository monitoredEndpointRepository;

  @Override
  public MonitoredEndpoint run(AddEndpointRequest request, User user) {
    MonitoredEndpoint endpoint = MonitoredEndpoint.fromNew(request.name(), request.path(), request.service(),
        user.getEmail());
    monitoredEndpointRepository.save(endpoint);

    return endpoint;
  }
}
