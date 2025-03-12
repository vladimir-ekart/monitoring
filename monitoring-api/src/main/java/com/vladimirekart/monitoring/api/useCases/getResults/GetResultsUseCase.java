package com.vladimirekart.monitoring.api.useCases.getResults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vladimirekart.monitoring.api.entity.MonitoredEndpoint;
import com.vladimirekart.monitoring.api.entity.MonitoringResult;
import com.vladimirekart.monitoring.api.entity.User;
import com.vladimirekart.monitoring.api.error.ForbiddenException;
import com.vladimirekart.monitoring.api.error.NotFoundException;
import com.vladimirekart.monitoring.api.repository.MonitoredEndpointRepository;
import com.vladimirekart.monitoring.api.repository.MonitoringResultRepository;
import com.vladimirekart.monitoring.api.useCases.UseCase;

@Service
public class GetResultsUseCase implements UseCase<GetResultsRequest, Iterable<MonitoringResult>> {
  @Autowired
  private MonitoredEndpointRepository monitoredEndpointRepository;

  @Autowired
  private MonitoringResultRepository monitoringResultRepository;

  @Override
  public Iterable<MonitoringResult> run(GetResultsRequest request, User user) {
    MonitoredEndpoint monitoredEndpoint = monitoredEndpointRepository.findById(request.endpointId())
        .orElseThrow(() -> new NotFoundException("Endpoint not found"));

    if (!monitoredEndpoint.isOwner(user))
      throw new ForbiddenException("User is not owner of this endpoint");

    return monitoringResultRepository.findTop10ByServiceAndPathOrderByCreatedAtDesc(monitoredEndpoint.getService(),
        monitoredEndpoint.getPath());
  }
}
