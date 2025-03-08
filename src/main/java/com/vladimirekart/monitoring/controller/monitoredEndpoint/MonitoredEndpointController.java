package com.vladimirekart.monitoring.controller.monitoredEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vladimirekart.monitoring.entity.MonitoredEndpoint;
import com.vladimirekart.monitoring.entity.User;
import com.vladimirekart.monitoring.useCases.addEndpoint.AddEndpointRequest;
import com.vladimirekart.monitoring.useCases.addEndpoint.AddEndpointUseCase;
import com.vladimirekart.monitoring.useCases.deleteEndpoint.DeleteEndpointRequest;
import com.vladimirekart.monitoring.useCases.deleteEndpoint.DeleteEndpointUseCase;
import com.vladimirekart.monitoring.useCases.getEndpoints.GetEndpointsUseCase;
import com.vladimirekart.monitoring.useCases.updateEndpoint.UpdateEndpointRequest;
import com.vladimirekart.monitoring.useCases.updateEndpoint.UpdateEndpointUseCase;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path="/monitored-endpoint")
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
  public @ResponseBody MonitoredEndpoint saveOne(HttpServletRequest request, @RequestBody AddEndpointRequest body) {
    User user = (User) request.getAttribute("user");

    return addEndpointUseCase.run(body, user);
  }

  @PutMapping(path="/{endpointId}")
  public @ResponseBody MonitoredEndpoint updateOne(HttpServletRequest request, @PathVariable Integer endpointId, @RequestBody UpdateEndpointRequestBody body) {
    User user = (User) request.getAttribute("user");
    UpdateEndpointRequest input = new UpdateEndpointRequest(body.name(), body.path(), body.service(), endpointId);

    try {
      return updateEndpointUseCase.run(input, user);
    } catch (Exception e) {
      throw new Error();
    }
  }

  @DeleteMapping(path="/{endpointId}")
  public @ResponseBody MonitoredEndpoint deleteOne(HttpServletRequest request, @PathVariable Integer endpointId) {
    User user = (User) request.getAttribute("user");
    DeleteEndpointRequest input = new DeleteEndpointRequest(endpointId);

    try {
      return deleteEndpointUseCase.run(input, user);
    } catch (Exception e) {
      throw new Error();
    }
  }
}
