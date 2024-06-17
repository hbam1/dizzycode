package com.dizzycode.dizzycode.controller;

import com.dizzycode.dizzycode.domain.Greeting;
import com.dizzycode.dizzycode.domain.HelloMessage;
import com.dizzycode.dizzycode.domain.Message;
import com.dizzycode.dizzycode.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
@RequiredArgsConstructor
public class GreetingController {

    private final MessageService messageService;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay

        Message message1 = new Message();
        message1.setMemberId("1");
        message1.setRoomId("1");
        message1.setChannelId("1");
        message1.setContent("aa");
        message1.setImageUrl("");
        messageService.saveMessage(message1);

        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}
