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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DirectMessageController {

    private final DirectMessageService directMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/direct/room/{roomId}")
    public MessageDetailDTO messageCreate(@DestinationVariable Long roomId,
                                          MessageCreateDTO messageCreateDTO) throws Exception {
        // 클라이언트에 리턴할 데이터
        MessageDetailDTO messageDetailDTO = directMessageService.saveDirectMessage(messageCreateDTO, roomId);

        log.info("messageCreate={}", messageCreateDTO.getContent());

        messagingTemplate.convertAndSend("/topic/direct/room/" + roomId);

        return messageDetailDTO;
    }

    @GetMapping("/direct/room/{roomId}/messages")
    public List<MessageDetailDTO> messageList(@PathVariable Long roomId, @RequestParam(name = "last", required = false) LocalDateTime last) {

        return directMessageService.messageList(roomId, last);
    }
}
