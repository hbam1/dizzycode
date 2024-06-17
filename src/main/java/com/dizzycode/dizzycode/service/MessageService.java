package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.Message;
import com.dizzycode.dizzycode.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }
}
