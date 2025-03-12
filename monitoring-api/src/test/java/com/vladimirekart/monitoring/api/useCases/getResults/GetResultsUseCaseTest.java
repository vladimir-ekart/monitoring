package com.vladimirekart.monitoring.api.useCases.getResults;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.github.javafaker.Faker;
import com.vladimirekart.monitoring.api.entity.MonitoredEndpoint;
import com.vladimirekart.monitoring.api.entity.MonitoringResult;
import com.vladimirekart.monitoring.api.entity.User;
import com.vladimirekart.monitoring.api.error.ForbiddenException;
import com.vladimirekart.monitoring.api.error.NotFoundException;
import com.vladimirekart.monitoring.api.repository.MonitoredEndpointRepository;
import com.vladimirekart.monitoring.api.repository.MonitoringResultRepository;

@SpringBootTest
@ActiveProfiles("test")
public class GetResultsUseCaseTest {
  @Autowired
  private GetResultsUseCase getResultsUseCase;

  @Autowired
  private MonitoredEndpointRepository monitoredEndpointRepository;

  @Autowired
  private MonitoringResultRepository monitoringResultRepository;

  private final Faker faker = new Faker();

  @Test
  void shouldGetResults() {
    User user = new User(faker.internet().emailAddress());

    MonitoredEndpoint endpoint = getOneMonitoredEndpoint(user);

    Iterable<MonitoringResult> existingResults = IntStream.range(0, 10).mapToObj(i -> MonitoringResult
        .fromNew(endpoint.getService(), endpoint.getPath(), faker.name().firstName(), faker.lorem().sentence(),
            faker.number().numberBetween(200, 599)))
        .collect(Collectors.toList());

    monitoredEndpointRepository.save(endpoint);
    monitoringResultRepository.saveAll(existingResults);

    GetResultsRequest request = new GetResultsRequest(endpoint.getId());

    Iterable<MonitoringResult> result = getResultsUseCase.run(request, user);

    List<MonitoringResult> resultList = StreamSupport.stream(result.spliterator(), false).toList();
    List<MonitoringResult> expectedList = StreamSupport.stream(existingResults.spliterator(), false).toList();

    assertEquals(10, resultList.size());
    for (MonitoringResult expected : expectedList) {
      assertTrue(resultList.stream().anyMatch(actual -> resultsMatch(expected, actual)));
    }
  }

  private boolean resultsMatch(MonitoringResult a, MonitoringResult b) {
    return Objects.equals(a.getService(), b.getService()) &&
        Objects.equals(a.getPath(), b.getPath()) &&
        Objects.equals(a.getPayload(), b.getPayload()) &&
        Objects.equals(a.getCreatedAt(), b.getCreatedAt()) &&
        Objects.equals(a.getMethod(), b.getMethod()) &&
        Objects.equals(a.getStatusCode(), b.getStatusCode());
  }

  @Test
  void shouldThrowNotFoundWhenEndpointDoesNotExist() {
    User user = new User(faker.internet().emailAddress());

    Integer nonExistingId = 99999;
    GetResultsRequest request = new GetResultsRequest(nonExistingId);

    assertThrows(NotFoundException.class, () -> {
      getResultsUseCase.run(request, user);
    });
  }

  @Test
  void shouldThrowForbiddenWhenUserIsNotOwner() {
    User user = new User(faker.internet().emailAddress());
    User anotherUser = new User(faker.internet().emailAddress());

    MonitoredEndpoint endpoint = getOneMonitoredEndpoint(user);
    monitoredEndpointRepository.save(endpoint);

    GetResultsRequest request = new GetResultsRequest(endpoint.getId());

    assertThrows(ForbiddenException.class, () -> {
      getResultsUseCase.run(request, anotherUser);
    });
  }

  private MonitoredEndpoint getOneMonitoredEndpoint(User user) {
    return MonitoredEndpoint.fromNew(faker.app().name(), "/" + faker.internet().slug(), faker.app().name(),
        user.getEmail());
  }
}