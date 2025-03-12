package com.vladimirekart.monitoring.api.useCases.addEndpoint;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.github.javafaker.Faker;
import com.vladimirekart.monitoring.api.entity.MonitoredEndpoint;
import com.vladimirekart.monitoring.api.entity.User;
import com.vladimirekart.monitoring.api.repository.MonitoredEndpointRepository;

@SpringBootTest
@ActiveProfiles("test")
public class AddEndpointUseCaseTest {
  @Autowired
  private AddEndpointUseCase addEndpointUseCase;

  @Autowired
  private MonitoredEndpointRepository monitoredEndpointRepository;

  private final Faker faker = new Faker();

  @Test
  void shouldAddEndpoint() {
    User user = new User(faker.internet().emailAddress());

    AddEndpointRequest request = new AddEndpointRequest(faker.app().name(), "/" + faker.internet().slug(),
        faker.app().name());

    MonitoredEndpoint result = addEndpointUseCase.run(request, user);

    MonitoredEndpoint savedEndpoint = monitoredEndpointRepository.findById(result.getId()).orElse(null);

    assertEquals(savedEndpoint.getName(), request.name());
    assertEquals(savedEndpoint.getPath(), request.path());
    assertEquals(savedEndpoint.getService(), request.service());
    assertEquals(savedEndpoint.getOwner(), user.getEmail());
  }
}
