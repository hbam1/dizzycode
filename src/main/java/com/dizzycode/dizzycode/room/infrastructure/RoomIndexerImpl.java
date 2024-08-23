package com.dizzycode.dizzycode.room.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomIndexerImpl implements RoomIndexer {

    private final RestTemplate restTemplate;

    @Value("${python.server.url}")
    private String pythonServerUrl;

    // 방 이름 벡터 index 생성 요청
    @Override
    public void addRoomIndex(Long roomId, String roomName) {
        String url = pythonServerUrl + "/add_room";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        String requestBody = String.format("{\"roomId\": \"%s\", \"roomName\": \"%s\"}", roomId, roomName);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.info("Indexing Failed : roomId={}", roomId);
        }
    }

    // 방 이름 벡터 index 삭제 요청
    @Override
    public void deleteRoomIndex(Long roomId) {
        String url = pythonServerUrl + "/delete_room";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        String requestBody = String.format("{\"roomId\": \"%s\"}", roomId);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.info("Index Deletion Failed : roomId={}", roomId);
        }
    }
}
