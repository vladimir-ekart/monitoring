package com.vladimirekart.monitoring.api.useCases.deleteEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vladimirekart.monitoring.api.entity.MonitoredEndpoint;
import com.vladimirekart.monitoring.api.entity.User;
import com.vladimirekart.monitoring.api.error.ForbiddenException;
import com.vladimirekart.monitoring.api.error.NotFoundException;
import com.vladimirekart.monitoring.api.repository.MonitoredEndpointRepository;
import com.vladimirekart.monitoring.api.useCases.UseCase;

@Service
public class DeleteEndpointUseCase implements UseCase<DeleteEndpointRequest, MonitoredEndpoint> {
  @Autowired
  private MonitoredEndpointRepository monitoredEndpointRepository;

  @Override
  public MonitoredEndpoint run(DeleteEndpointRequest request, User user) {
    MonitoredEndpoint existingEndpoint = monitoredEndpointRepository.findById(request.endpointId()).orElseThrow(() -> new NotFoundException("Endpoint not found"));

    if (!existingEndpoint.isOwner(user)) throw new ForbiddenException("User is not owner of this endpoint");

    monitoredEndpointRepository.delete(existingEndpoint);

    return existingEndpoint;
  }
}
