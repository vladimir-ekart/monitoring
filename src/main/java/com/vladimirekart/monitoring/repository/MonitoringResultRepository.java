package com.vladimirekart.monitoring.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.vladimirekart.monitoring.entity.MonitoringResult;

public interface MonitoringResultRepository extends CrudRepository<MonitoringResult, Integer> {
  List<MonitoringResult> findTop10ByServiceAndPathOrderByCreatedAtDesc(String service, String path);
}
