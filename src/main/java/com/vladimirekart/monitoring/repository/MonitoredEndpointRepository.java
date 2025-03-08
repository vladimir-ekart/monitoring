package com.vladimirekart.monitoring.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.vladimirekart.monitoring.entity.MonitoredEndpoint;

public interface MonitoredEndpointRepository extends CrudRepository<MonitoredEndpoint, Integer> {
  List<MonitoredEndpoint> findByOwner(String owner);
}
