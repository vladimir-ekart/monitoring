package com.vladimirekart.monitoring.api.controller.monitoringResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vladimirekart.monitoring.api.entity.MonitoringResult;
import com.vladimirekart.monitoring.api.entity.User;
import com.vladimirekart.monitoring.api.useCases.getResults.GetResultsRequest;
import com.vladimirekart.monitoring.api.useCases.getResults.GetResultsUseCase;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path="/monitored-endpoint/{endpointId}/monitoring-result")
public class MonitoringResultController {
  @Autowired
  private GetResultsUseCase getResultsUseCase;

  @GetMapping()
  public @ResponseBody Iterable<MonitoringResult> getAll(HttpServletRequest request, @PathVariable("endpointId") Integer endpointId) {
    User user = (User) request.getAttribute("user");

    try {
      return getResultsUseCase.run(new GetResultsRequest(endpointId), user);
    } catch (Exception e) {
      throw new Error();
    }
  }
}
