package com.vladimirekart.monitoring.api.controller.monitoredEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vladimirekart.monitoring.api.entity.MonitoredEndpoint;
import com.vladimirekart.monitoring.api.entity.User;
import com.vladimirekart.monitoring.api.useCases.addEndpoint.AddEndpointRequest;
import com.vladimirekart.monitoring.api.useCases.addEndpoint.AddEndpointUseCase;
import com.vladimirekart.monitoring.api.useCases.deleteEndpoint.DeleteEndpointRequest;
import com.vladimirekart.monitoring.api.useCases.deleteEndpoint.DeleteEndpointUseCase;
import com.vladimirekart.monitoring.api.useCases.getEndpoints.GetEndpointsUseCase;
import com.vladimirekart.monitoring.api.useCases.updateEndpoint.UpdateEndpointRequest;
import com.vladimirekart.monitoring.api.useCases.updateEndpoint.UpdateEndpointUseCase;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/monitored-endpoint")
public class MonitoredEndpointController {
  @Autowired 
  private AddEndpointUseCase addEndpointUseCase;

  @Autowired
  private GetEndpointsUseCase getEndpointsUseCase;

  @Autowired
  private UpdateEndpointUseCase updateEndpointUseCase;

  @Autowired
  private DeleteEndpointUseCase deleteEndpointUseCase;

  @GetMapping()
  public @ResponseBody Iterable<MonitoredEndpoint> getAll(HttpServletRequest request) {
    User user = (User) request.getAttribute("user");

    return getEndpointsUseCase.run(null, user);
  }

  @PostMapping()
  public @ResponseBody MonitoredEndpoint saveOne(HttpServletRequest request, @RequestBody @Valid AddEndpointRequest body) {
    User user = (User) request.getAttribute("user");

    return addEndpointUseCase.run(body, user);
  }

  @PutMapping(path = "/{endpointId}")
  public @ResponseBody MonitoredEndpoint updateOne(HttpServletRequest request, @PathVariable("endpointId") Integer endpointId, @RequestBody @Valid UpdateEndpointRequestBody body) {
    User user = (User) request.getAttribute("user");
    UpdateEndpointRequest input = new UpdateEndpointRequest(body.name(), body.path(), body.service(), endpointId);

    return updateEndpointUseCase.run(input, user);
  }

  @DeleteMapping(path = "/{endpointId}")
  public @ResponseBody MonitoredEndpoint deleteOne(HttpServletRequest request, @PathVariable("endpointId") Integer endpointId) {
    User user = (User) request.getAttribute("user");
    DeleteEndpointRequest input = new DeleteEndpointRequest(endpointId);

    return deleteEndpointUseCase.run(input, user);
  }
}
