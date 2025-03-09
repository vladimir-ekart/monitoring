package com.vladimirekart.monitoring.api.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.vladimirekart.monitoring.api.entity.MonitoredEndpoint;

public interface MonitoredEndpointRepository extends CrudRepository<MonitoredEndpoint, Integer> {
  List<MonitoredEndpoint> findByOwner(String owner);
}
