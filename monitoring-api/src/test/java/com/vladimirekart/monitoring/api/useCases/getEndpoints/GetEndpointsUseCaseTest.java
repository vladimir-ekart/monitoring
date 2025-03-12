package com.vladimirekart.monitoring.api.useCases.getEndpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.vladimirekart.monitoring.api.entity.User;
import com.vladimirekart.monitoring.api.repository.MonitoredEndpointRepository;

@SpringBootTest
@ActiveProfiles("test")
public class GetEndpointsUseCaseTest {
  @Autowired
  private GetEndpointsUseCase getEndpointsUseCase;

  @Autowired
  private MonitoredEndpointRepository monitoredEndpointRepository;

  private final Faker faker = new Faker();

  @Test
  void shouldGetEndpoints() {
    User user = new User(faker.internet().emailAddress());

    Iterable<MonitoredEndpoint> existingEndpoints = IntStream.range(0, 10).mapToObj(i -> MonitoredEndpoint
        .fromNew(faker.app().name(), "/" + faker.internet().slug(), faker.app().name(), user.getEmail()))
        .collect(Collectors.toList());

    monitoredEndpointRepository.saveAll(existingEndpoints);

    Iterable<MonitoredEndpoint> result = getEndpointsUseCase.run(null, user);

    List<MonitoredEndpoint> resultList = StreamSupport.stream(result.spliterator(), false).toList();
    List<MonitoredEndpoint> expectedList = StreamSupport.stream(existingEndpoints.spliterator(), false).toList();

    assertEquals(10, resultList.size());
    for (MonitoredEndpoint expected : expectedList) {
      assertTrue(resultList.stream().anyMatch(actual -> endpointsMatch(expected, actual)));
    }
  }

  private boolean endpointsMatch(MonitoredEndpoint a, MonitoredEndpoint b) {
    return Objects.equals(a.getName(), b.getName()) &&
        Objects.equals(a.getPath(), b.getPath()) &&
        Objects.equals(a.getService(), b.getService()) &&
        Objects.equals(a.getOwner(), b.getOwner());
  }
}
