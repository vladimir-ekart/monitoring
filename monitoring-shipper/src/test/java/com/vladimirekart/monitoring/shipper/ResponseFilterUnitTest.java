package com.vladimirekart.monitoring.shipper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.github.javafaker.Faker;

@ActiveProfiles("test")
public class ResponseFilterUnitTest {

    private ResponseFilter filter;
    private RestTemplate restTemplateMock;
    private HttpServletRequest requestMock;
    private FilterChain filterChainMock;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        restTemplateMock = mock(RestTemplate.class);
        filter = new ResponseFilterTestable(restTemplateMock);

        filter.service = faker.app().name();
        filter.collectorUrl = faker.internet().url();

        requestMock = mock(HttpServletRequest.class);
        filterChainMock = mock(FilterChain.class);
    }

    @Test
    void shouldSendMonitoringResult() throws IOException, ServletException {
        when(requestMock.getRequestURI()).thenReturn(filter.collectorUrl);
        when(requestMock.getMethod()).thenReturn("GET");

        MockHttpServletResponse realResponse = new MockHttpServletResponse();

        doAnswer(invocation -> {
            ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) invocation.getArgument(1);
            responseWrapper.setStatus(200);
            PrintWriter writer = responseWrapper.getWriter();
            writer.write("response-body");
            writer.flush();
            return null;
        }).when(filterChainMock).doFilter(any(), any());

        filter.doFilter(requestMock, realResponse, filterChainMock);

        ArgumentCaptor<HttpEntity<MonitoringResult>> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplateMock).postForEntity(eq(filter.collectorUrl), captor.capture(), eq(String.class));

        HttpEntity<MonitoringResult> sentEntity = captor.getValue();
        MonitoringResult sentResult = sentEntity.getBody();

        assertEquals(filter.service, sentResult.getService());
        assertEquals(filter.collectorUrl, sentResult.getPath());
        assertEquals("response-body", sentResult.getPayload());
        assertEquals("GET", sentResult.getMethod());
        assertEquals(200, sentResult.getStatusCode());

        HttpHeaders headers = sentEntity.getHeaders();
        assert headers.getContentType().equals(MediaType.APPLICATION_JSON);
    }

    static class ResponseFilterTestable extends ResponseFilter {
        private final RestTemplate injectedRestTemplate;

        public ResponseFilterTestable(RestTemplate restTemplate) {
            this.injectedRestTemplate = restTemplate;
        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
                throws IOException, ServletException {
            this.restTemplate = injectedRestTemplate;
            super.doFilter(servletRequest, servletResponse, filterChain);
        }
    }
}
