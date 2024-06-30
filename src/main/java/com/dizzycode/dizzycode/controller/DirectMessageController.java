package com.dizzycode.dizzycode.controller;

import com.dizzycode.dizzycode.domain.DirectMessage;
import com.dizzycode.dizzycode.dto.message.DirectMessageDetailDTO;
import com.dizzycode.dizzycode.dto.message.MessageCreateDTO;
import com.dizzycode.dizzycode.dto.message.MessageDetailDTO;
import com.dizzycode.dizzycode.service.DirectMessageService;
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
public class DirectMessageController {

    private final DirectMessageService directMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/direct/receiver/{receiverId}")
    public MessageDetailDTO messageCreate(@DestinationVariable Long receiverId,
                                          MessageCreateDTO messageCreateDTO) throws Exception {
        // 클라이언트에 리턴할 데이터
        DirectMessageDetailDTO messageDetailDTO = directMessageService.saveDirectMessage(messageCreateDTO, receiverId);

        log.info("messageCreate={}", messageCreateDTO.getContent());

        String friendshipId = messageDetailDTO.getFriendshipId();
        messagingTemplate.convertAndSend("/topic/direct/" + friendshipId);

        return messageDetailDTO;
    }
}
