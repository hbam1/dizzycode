package com.dizzycode.dizzycode.controller;

import com.dizzycode.dizzycode.member.domain.MemberStatus;
import com.dizzycode.dizzycode.dto.room.RoomMemberStatusDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberStatusController {

    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    // websocket 연결 직후, 혹은 직접 연결 끊은 후에 요청
    @MessageMapping("rooms/{roomId}/status")
    public void onlineStatus(@DestinationVariable Long roomId,
                             RoomMemberStatusDTO roomMemberStatusDTO) {

        // 각 방에 들어가 있는 사람들에게 실시간으로 유저의 상태를 업데이트 해주기 위해서 event 발행
        rabbitTemplate.convertAndSend("amq.topic", "rooms." + roomId + ".status", roomMemberStatusDTO);
    }

    @MessageMapping("direct/rooms/{roomId]/status")
    public void dmOnlineStatus(@DestinationVariable Long roomId,
                               RoomMemberStatusDTO roomMemberStatusDTO) {

        rabbitTemplate.convertAndSend("amq.topic", "direct.rooms." + roomId + ".status", roomMemberStatusDTO);
    }

    @MessageMapping("friendship/status/member1/{memberId1}/member2/{memberId2}")
    public void friendOnlineStatus(@DestinationVariable Long memberId1, Long memberId2,
                                   MemberStatus memberStatus) {

        String friendshipId;
        if (memberId1 < memberId2) {
            friendshipId = memberId1 + "-" + memberId2;
        } else {
            friendshipId = memberId2 + "-" + memberId1;
        }

        rabbitTemplate.convertAndSend("amq.topic", "friendship." + friendshipId + ".status", memberStatus);
    }

    @MessageMapping("members/status/heartbeat")
    public void statusHeartbeat(RoomMemberStatusDTO roomMemberStatusDTO) {
        // 10초 간격으로 접속 상태 heartbeat
        HashMap<String, String> userStatus = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        userStatus.put("status", roomMemberStatusDTO.getStatus());
        userStatus.put("lastActive", now.toString());

        log.info("now={}", now);
        redisTemplate.opsForHash().putAll("memberId:" + roomMemberStatusDTO.getMemberId(), userStatus);
    }
}