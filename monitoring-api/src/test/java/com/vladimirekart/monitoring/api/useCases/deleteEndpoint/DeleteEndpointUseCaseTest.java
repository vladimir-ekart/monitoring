package com.vladimirekart.monitoring.api.useCases.deleteEndpoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.github.javafaker.Faker;
import com.vladimirekart.monitoring.api.entity.MonitoredEndpoint;
import com.vladimirekart.monitoring.api.entity.User;
import com.vladimirekart.monitoring.api.error.ForbiddenException;
import com.vladimirekart.monitoring.api.error.NotFoundException;
import com.vladimirekart.monitoring.api.repository.MonitoredEndpointRepository;

@SpringBootTest
@ActiveProfiles("test")
public class DeleteEndpointUseCaseTest {
  @Autowired
  private DeleteEndpointUseCase deleteEndpointUseCase;

  @Autowired
  private MonitoredEndpointRepository monitoredEndpointRepository;

  private final Faker faker = new Faker();

  @Test
  void shouldDeleteEndpoint() {
    User user = new User(faker.internet().emailAddress());
    MonitoredEndpoint existingEndpoint = getOneMonitoredEndpoint(user);

    monitoredEndpointRepository.save(existingEndpoint);

    DeleteEndpointRequest request = new DeleteEndpointRequest(existingEndpoint.getId());

    deleteEndpointUseCase.run(request, user);

    MonitoredEndpoint endpointAfterDelete = monitoredEndpointRepository.findById(existingEndpoint.getId()).orElse(null);

    assertEquals(endpointAfterDelete, null);
  }

  @Test
  void shouldThrowNotFoundWhenEndpointDoesNotExist() {
    User user = new User(faker.internet().emailAddress());

    Integer nonExistingId = 99999;

    DeleteEndpointRequest request = new DeleteEndpointRequest(nonExistingId);

    assertThrows(NotFoundException.class, () -> {
      deleteEndpointUseCase.run(request, user);
    });
  }

  @Test
  void shouldThrowForbiddenWhenUserIsNotOwner() {
    User user = new User(faker.internet().emailAddress());
    User anotherUser = new User(faker.internet().emailAddress());

    MonitoredEndpoint endpoint = getOneMonitoredEndpoint(user);
    monitoredEndpointRepository.save(endpoint);

    DeleteEndpointRequest request = new DeleteEndpointRequest(endpoint.getId());

    assertThrows(ForbiddenException.class, () -> {
      deleteEndpointUseCase.run(request, anotherUser);
    });
  }

  private MonitoredEndpoint getOneMonitoredEndpoint(User user) {
    return MonitoredEndpoint.fromNew(faker.app().name(), "/" + faker.internet().slug(), faker.app().name(),
        user.getEmail());
  }
}
