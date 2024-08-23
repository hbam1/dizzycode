package com.dizzycode.dizzycode.recommendation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    @Value("${python.server.url}")
    private String pythonServerUrl;

    private final RestTemplate restTemplate;

    public List<Map<String, Object>> getRoomRecommendations(String keyword, int topN) {
        String url = pythonServerUrl + "/search";
        Map<String, Object> requestBody = Map.of("query", keyword, "k", topN);
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(requestBody),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        return response.getBody();
    }
}

