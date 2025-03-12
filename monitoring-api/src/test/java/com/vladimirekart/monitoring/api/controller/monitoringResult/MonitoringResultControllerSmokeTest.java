package com.vladimirekart.monitoring.api.controller.monitoringResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.github.javafaker.Faker;
import com.vladimirekart.monitoring.api.entity.MonitoredEndpoint;
import com.vladimirekart.monitoring.api.entity.MonitoringResult;
import com.vladimirekart.monitoring.api.entity.User;
import com.vladimirekart.monitoring.api.repository.MonitoredEndpointRepository;
import com.vladimirekart.monitoring.api.repository.MonitoringResultRepository;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MonitoringResultControllerSmokeTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MonitoringResultController controller;

  @Autowired
  private MonitoredEndpointRepository monitoredEndpointRepository;

  @Autowired
  private MonitoringResultRepository monitoringResultRepository;

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
  void shouldGetMonitoringResults() throws Exception {
    MonitoredEndpoint existingEndpoint = MonitoredEndpoint.fromNew(
        faker.app().name(), "/" + faker.internet().slug(), faker.app().name(), user.getEmail());
    monitoredEndpointRepository.save(existingEndpoint);

    MonitoringResult existingResult = MonitoringResult
        .fromNew(existingEndpoint.getService(), existingEndpoint.getPath(), faker.app().name(),
            faker.lorem().sentence(),
            faker.number().numberBetween(200, 599));
    monitoringResultRepository.save(existingResult);

    mockMvc.perform(get("/monitored-endpoint/{endpointId}/monitoring-result", existingEndpoint.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .requestAttr("user", user))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].statusCode").isNotEmpty())
        .andExpect(jsonPath("$[0].payload").isNotEmpty());
  }
}
