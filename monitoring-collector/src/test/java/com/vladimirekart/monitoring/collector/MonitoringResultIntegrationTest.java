package com.vladimirekart.monitoring.collector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MonitoringResultIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MonitoringResultController controller;

  @Autowired
  private MonitoringResultRepository monitoringResultRepository;

  private final Faker faker = new Faker();

  @Test
  void contextLoads() throws Exception {
    assertThat(controller).isNotNull();
  }

  @Test
  void shouldSaveMonitoringResult() throws Exception {
    MonitoringResult request = new MonitoringResult(
        faker.app().name(),
        "/" + faker.internet().slug(),
        faker.lorem().sentence(),
        faker.app().name(),
        new Date(),
        faker.number().numberBetween(200, 599));

    mockMvc.perform(post("/monitoring-result")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    MonitoringResult savedResult = monitoringResultRepository.findTop10ByServiceAndPathOrderByCreatedAtDesc(request.getService(), request.getPath()).get(0);

    assertEquals(savedResult.getService(), request.getService());
    assertEquals(savedResult.getPath(), request.getPath());
    assertEquals(savedResult.getPayload(), request.getPayload());
    assertEquals(savedResult.getMethod(), request.getMethod());
    assertEquals(savedResult.getStatusCode(), request.getStatusCode());
  }
}