package com.vladimirekart.monitoring.api.controller.monitoredEndpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.vladimirekart.monitoring.api.entity.MonitoredEndpoint;
import com.vladimirekart.monitoring.api.entity.User;
import com.vladimirekart.monitoring.api.repository.MonitoredEndpointRepository;
import com.vladimirekart.monitoring.api.useCases.addEndpoint.AddEndpointRequest;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MonitoredEndpointControllerSmokeTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  MonitoredEndpointController controller;

  @Autowired
  private MonitoredEndpointRepository monitoredEndpointRepository;

  private final Faker faker = new Faker();
  private User user;

  @BeforeEach
  void setUp() {
    user = new User(faker.internet().emailAddress());
  }

  @Test
  void contextLoads() throws Exception {
    assertThat(controller).isNotNull();
  }

  @Test
  void shouldAddEndpoint() throws Exception {
    MonitoredEndpoint endpoint = getOneMonitoredEndpoint(user);
    AddEndpointRequest request = new AddEndpointRequest(endpoint.getName(), endpoint.getPath(), endpoint.getService());

    mockMvc.perform(post("/monitored-endpoint")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
        .requestAttr("user", user))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(request.name()))
        .andExpect(jsonPath("$.path").value(request.path()))
        .andExpect(jsonPath("$.service").value(request.service()));
  }

  @Test
  void shouldGetEndpoints() throws Exception {
    MonitoredEndpoint endpoint = getOneMonitoredEndpoint(user);

    monitoredEndpointRepository.save(endpoint);

    mockMvc.perform(get("/monitored-endpoint")
        .requestAttr("user", user))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(endpoint.getId()));
  }

  @Test
  void shouldUpdateEndpoint() throws Exception {
    MonitoredEndpoint existingEndpoint = getOneMonitoredEndpoint(user);
    monitoredEndpointRepository.save(existingEndpoint);

    MonitoredEndpoint updateEndpoint = getOneMonitoredEndpoint(user);
    UpdateEndpointRequestBody updateRequest = new UpdateEndpointRequestBody(updateEndpoint.getName(),
        updateEndpoint.getPath(), updateEndpoint.getService());

    mockMvc.perform(put("/monitored-endpoint/{endpointId}", existingEndpoint.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateRequest))
        .requestAttr("user", user))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(updateRequest.name()))
        .andExpect(jsonPath("$.path").value(updateRequest.path()))
        .andExpect(jsonPath("$.service").value(updateRequest.service()));
  }

  @Test
  void shouldDeleteEndpoint() throws Exception {
    MonitoredEndpoint existingEndpoint = getOneMonitoredEndpoint(user);
    monitoredEndpointRepository.save(existingEndpoint);

    mockMvc.perform(delete("/monitored-endpoint/{endpointId}", existingEndpoint.getId())
        .requestAttr("user", user))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(existingEndpoint.getId()));

    MonitoredEndpoint endpointAfterDelete = monitoredEndpointRepository.findById(existingEndpoint.getId()).orElse(null);

    assertEquals(endpointAfterDelete, null);
  }

  private MonitoredEndpoint getOneMonitoredEndpoint(User user) {
    return MonitoredEndpoint.fromNew(faker.app().name(), "/" + faker.internet().slug(), faker.app().name(),
        user.getEmail());
  }
}
