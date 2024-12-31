package com.clara.discogschallenge.infrastructure.adapter.client;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.clara.discogschallenge.domain.model.Release;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class DiscogsClientTest {

    @Mock
    private RestTemplate restTemplate;

    private DiscogsClient discogsClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        discogsClient = new DiscogsClient();
        ReflectionTestUtils.setField(discogsClient, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(discogsClient, "objectMapper", objectMapper);
    }

    @Test
    void getArtistReleasesWhenApiErrorShouldReturnEmptyList() {
        when(restTemplate.exchange(
            any(String.class),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(String.class)
        )).thenThrow(new RuntimeException("API Error"));

        List<Release> releases = discogsClient.getArtistReleases("123", 1, 10);

        assertNotNull(releases);
        assertTrue(releases.isEmpty());
    }
}
