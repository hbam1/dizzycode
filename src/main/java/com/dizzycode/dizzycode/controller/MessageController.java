package com.dizzycode.dizzycode.controller;

import com.dizzycode.dizzycode.domain.DirectMessage;
import com.dizzycode.dizzycode.dto.message.MessageCreateDTO;
import com.dizzycode.dizzycode.dto.message.MessageDetailDTO;
import com.dizzycode.dizzycode.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/rooms/{roomId}/categories/{categoryId}/channels/{channelId}")
    public MessageDetailDTO messageCreate(@DestinationVariable Long roomId,
                             @DestinationVariable Long categoryId,
                             @DestinationVariable Long channelId,
                             MessageCreateDTO messageCreateDTO) throws Exception {
        // 채널 구독자들에게 보낼 메시지 데이터
        MessageDetailDTO messageDetailDTO = messageService.saveMessage(messageCreateDTO, roomId, categoryId, channelId);

        // 방 구독자들에게 보낼 메시지 데이터
        

        log.info("messageCreate={}", messageCreateDTO.getContent());

        // Send the message to the room topic
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId, messageDetailDTO);

        // Send the message to the channel topic
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId + "/categories/" + categoryId + "/channels/" + channelId, messageDetailDTO);


        return messageDetailDTO;
    }
}
