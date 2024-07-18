package com.dizzycode.dizzycode.controller;

import com.dizzycode.dizzycode.dto.room.RoomMemberStatusDTO;
import com.dizzycode.dizzycode.dto.room.RoomMemberUsernameDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberStatusController {

    private final RabbitTemplate rabbitTemplate;

    @MessageMapping("rooms/{roomId}/status")
    public void onlineStatus(@DestinationVariable Long roomId,
                             RoomMemberStatusDTO roomMemberStatusDTO) {

        rabbitTemplate.convertAndSend("amq.topic", "rooms." + roomId + ".status", roomMemberStatusDTO);
    }
}
