package com.dizzycode.dizzycode.controller;

import com.dizzycode.dizzycode.domain.Greeting;
import com.dizzycode.dizzycode.domain.HelloMessage;
import com.dizzycode.dizzycode.domain.Message;
import com.dizzycode.dizzycode.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
@RequiredArgsConstructor
public class GreetingController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/rooms/{roomId}/categories/{categoryId}/channels/{channelId}")
    public Greeting greeting(@DestinationVariable String roomId,
                             @DestinationVariable String categoryId,
                             @DestinationVariable String channelId,
                             HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay

        Message message1 = new Message();
        message1.setMemberId("1");
        message1.setRoomId("1");
        message1.setChannelId("1");
        message1.setContent("aa");
        message1.setImageUrl("");
        messageService.saveMessage(message1);

        Greeting greeting = new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");


        // Send the message to the room topic
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId, greeting);

        // Send the message to the channel topic
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId + "/categories/" + categoryId + "/channels/" + channelId, greeting);


        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}
