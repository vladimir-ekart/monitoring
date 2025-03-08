package com.vladimirekart.monitoring.useCases.updateEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vladimirekart.monitoring.entity.MonitoredEndpoint;
import com.vladimirekart.monitoring.entity.User;
import com.vladimirekart.monitoring.repository.MonitoredEndpointRepository;
import com.vladimirekart.monitoring.useCases.UseCase;

@Service
public class UpdateEndpointUseCase implements UseCase<UpdateEndpointRequest, MonitoredEndpoint> {
  @Autowired
  private MonitoredEndpointRepository monitoredEndpointRepository;

  @Override
  public MonitoredEndpoint run(UpdateEndpointRequest request, User user) throws Exception {
    MonitoredEndpoint existingEndpoint = monitoredEndpointRepository.findById(request.endpointId()).get();

    if (!existingEndpoint.isOwner(user)) throw new Exception("User is not owner of this endpoint");

    existingEndpoint.updateBaseInfo(request.name(), request.path(), request.service());

    monitoredEndpointRepository.save(existingEndpoint);

    return existingEndpoint;
  }
}
