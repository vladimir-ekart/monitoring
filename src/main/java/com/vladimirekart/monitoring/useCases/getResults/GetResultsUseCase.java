package com.vladimirekart.monitoring.useCases.getResults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vladimirekart.monitoring.entity.MonitoredEndpoint;
import com.vladimirekart.monitoring.entity.MonitoringResult;
import com.vladimirekart.monitoring.entity.User;
import com.vladimirekart.monitoring.repository.MonitoredEndpointRepository;
import com.vladimirekart.monitoring.repository.MonitoringResultRepository;
import com.vladimirekart.monitoring.useCases.UseCase;

@Service
public class GetResultsUseCase implements UseCase<GetResultsRequest, Iterable<MonitoringResult>> {
  @Autowired
  private MonitoredEndpointRepository monitoredEndpointRepository;

  @Autowired
  private MonitoringResultRepository monitoringResultRepository;

  @Override
  public Iterable<MonitoringResult> run(GetResultsRequest request, User user) throws Exception {
    MonitoredEndpoint monitoredEndpoint = monitoredEndpointRepository.findById(request.endpointId()).get();

    if (!monitoredEndpoint.isOwner(user)) throw new Exception("User is not owner of this endpoint");

    return monitoringResultRepository.findTop10ByServiceAndPathOrderByCreatedAtDesc(monitoredEndpoint.getService(), monitoredEndpoint.getPath());
  }
}
