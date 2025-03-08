package com.vladimirekart.monitoring.useCases.deleteEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vladimirekart.monitoring.entity.MonitoredEndpoint;
import com.vladimirekart.monitoring.entity.User;
import com.vladimirekart.monitoring.repository.MonitoredEndpointRepository;
import com.vladimirekart.monitoring.useCases.UseCase;

@Service
public class DeleteEndpointUseCase implements UseCase<DeleteEndpointRequest, MonitoredEndpoint> {
  @Autowired
  private MonitoredEndpointRepository monitoredEndpointRepository;

  @Override
  public MonitoredEndpoint run(DeleteEndpointRequest request, User user) throws Exception {
    MonitoredEndpoint existingEndpoint = monitoredEndpointRepository.findById(request.endpointId()).get();

    if (!existingEndpoint.isOwner(user)) throw new Exception("User is not owner of this endpoint");

    monitoredEndpointRepository.delete(existingEndpoint);

    return existingEndpoint;
  }
}
