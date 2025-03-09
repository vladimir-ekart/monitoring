package com.vladimirekart.monitoring.collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/monitoring-result")
public class MonitoringResultController {
  @Autowired
  private MonitoringResultRepository monitoringResultRepository;

  @PostMapping()
  public @ResponseBody void saveOne(@RequestBody MonitoringResult monitoringResult) {
    monitoringResultRepository.save(monitoringResult);
  }
}
