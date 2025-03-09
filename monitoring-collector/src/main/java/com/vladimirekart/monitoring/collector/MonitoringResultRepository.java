package com.vladimirekart.monitoring.collector;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface MonitoringResultRepository extends CrudRepository<MonitoringResult, Integer> {
  List<MonitoringResult> findTop10ByServiceAndPathOrderByCreatedAtDesc(String service, String path);
}

