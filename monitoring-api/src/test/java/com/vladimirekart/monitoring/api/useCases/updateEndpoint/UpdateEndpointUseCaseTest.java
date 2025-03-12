package com.vladimirekart.monitoring.api.useCases.updateEndpoint;

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
public class UpdateEndpointUseCaseTest {
  @Autowired
  private UpdateEndpointUseCase updateEndpointUseCase;

  @Autowired
  private MonitoredEndpointRepository monitoredEndpointRepository;

  private final Faker faker = new Faker();

  @Test
  void shouldUpdateEndpoint() {
    User user = new User(faker.internet().emailAddress());
    MonitoredEndpoint existingEndpoint = getOneMonitoredEndpoint(user);

    monitoredEndpointRepository.save(existingEndpoint);

    MonitoredEndpoint updateEndpoint = getOneMonitoredEndpoint(user);
    UpdateEndpointRequest request = new UpdateEndpointRequest(
        updateEndpoint.getName(),
        updateEndpoint.getPath(),
        updateEndpoint.getService(),
        existingEndpoint.getId());

    monitoredEndpointRepository.save(existingEndpoint);

    updateEndpointUseCase.run(request, user);

    MonitoredEndpoint endpointAfterUpdate = monitoredEndpointRepository.findById(existingEndpoint.getId()).orElse(null);

    assertEquals(endpointAfterUpdate.getName(), request.name());
    assertEquals(endpointAfterUpdate.getPath(), request.path());
    assertEquals(endpointAfterUpdate.getService(), request.service());
    assertEquals(endpointAfterUpdate.getOwner(), user.getEmail());
  }

  @Test
  void shouldThrowNotFoundWhenEndpointDoesNotExist() {
    User user = new User(faker.internet().emailAddress());

    Integer nonExistingId = 99999;
    MonitoredEndpoint updateEndpoint = getOneMonitoredEndpoint(user);
    UpdateEndpointRequest request = new UpdateEndpointRequest(
        updateEndpoint.getName(),
        updateEndpoint.getPath(),
        updateEndpoint.getService(),
        nonExistingId);

    assertThrows(NotFoundException.class, () -> {
      updateEndpointUseCase.run(request, user);
    });
  }

  @Test
  void shouldThrowForbiddenWhenUserIsNotOwner() {
    User user = new User(faker.internet().emailAddress());
    User anothUser = new User(faker.internet().emailAddress());

    MonitoredEndpoint existingEndpoint = getOneMonitoredEndpoint(user);
    monitoredEndpointRepository.save(existingEndpoint);

    MonitoredEndpoint updateEndpoint = getOneMonitoredEndpoint(user);
    UpdateEndpointRequest request = new UpdateEndpointRequest(
        updateEndpoint.getName(),
        updateEndpoint.getPath(),
        updateEndpoint.getService(),
        existingEndpoint.getId());

    assertThrows(ForbiddenException.class, () -> {
      updateEndpointUseCase.run(request, anothUser);
    });
  }

  private MonitoredEndpoint getOneMonitoredEndpoint(User user) {
    return MonitoredEndpoint.fromNew(faker.app().name(), "/" + faker.internet().slug(), faker.app().name(),
        user.getEmail());
  }
}
