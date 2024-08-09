package com.dizzycode.dizzycode.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class HeartbeatScheduler {

    private final RedisTemplate<String, String> redisTemplate;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;


//    @Scheduled(fixedRate = 5000)
//    public void checkHeartbeat() {
//        Set<String> keys = redisTemplate.keys("memberId:*");
//
//        if (keys != null) {
//            LocalDateTime currentTime = LocalDateTime.now();
//            long thresholdSeconds = 60; // 1분
//
//            for (String key : keys) {
//                String lastActiveStr = (String) redisTemplate.opsForHash().get(key, "lastActive");
//                if (lastActiveStr != null) {
//                    LocalDateTime lastActive = LocalDateTime.parse(lastActiveStr, formatter);
//                    if (lastActive.plusSeconds(thresholdSeconds).isBefore(currentTime)) {
//                        // 상태를 offline으로 변경
//                        redisTemplate.opsForHash().put(key, "status", "offline");
//                    }
//                }
//            }
//        }
//    }
}
