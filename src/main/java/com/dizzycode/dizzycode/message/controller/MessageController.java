package com.dizzycode.dizzycode.message.controller;

import com.dizzycode.dizzycode.message.domain.dto.MessageCreateDTO;
import com.dizzycode.dizzycode.message.domain.dto.MessageDetailDTO;
import com.dizzycode.dizzycode.message.domain.dto.MessageRoomDTO;
import com.dizzycode.dizzycode.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;
    private final RabbitTemplate rabbitTemplate;

    @MessageMapping("rooms.{roomId}.categories.{categoryId}.channels.{channelId}")
    public MessageDetailDTO messageCreate(@DestinationVariable Long roomId,
                             @DestinationVariable Long categoryId,
                             @DestinationVariable Long channelId,
                             MessageCreateDTO messageCreateDTO) throws Exception {
        // 채널 구독자들에게 보낼 메시지 데이터
        MessageDetailDTO messageDetailDTO = messageService.saveMessage(messageCreateDTO, roomId, categoryId, channelId);

        // 방 구독자들에게 보낼 메시지 데이터
        MessageRoomDTO messageRoomDTO = new MessageRoomDTO();
        messageRoomDTO.setRoomId(roomId);
        messageRoomDTO.setCategoryId(categoryId);
        messageRoomDTO.setChannelId(channelId);

        log.info("messageCreate={}", messageCreateDTO.getContent());

        // Send the message to RabbitMQ for the room
        rabbitTemplate.convertAndSend("amq.topic", "rooms." + roomId, messageRoomDTO);
        // Send the message to RabbitMQ for the channel
        rabbitTemplate.convertAndSend("amq.topic", "rooms." + roomId + ".categories." + categoryId + ".channels." + channelId, messageDetailDTO);

        return messageDetailDTO;
    }

    @GetMapping("/rooms/{roomId}/categories/{categoryId}/channels/{channelId}/messages")
    public List<MessageDetailDTO> messageList(@PathVariable Long roomId, @PathVariable Long categoryId, @PathVariable Long channelId, @RequestParam(name = "last", required = false) LocalDateTime last) {

        return messageService.messageList(channelId, last, roomId);
    }
}
