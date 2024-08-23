package com.dizzycode.dizzycode.recommendation.controller;

import com.dizzycode.dizzycode.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/recommend")
    public List<Map<String, Object>> recommend(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "20") int topN
    ) {
        return recommendationService.getRoomRecommendations(keyword, topN);
    }
}
