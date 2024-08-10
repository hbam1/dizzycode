package com.dizzycode.dizzycode.message.service;

import com.dizzycode.dizzycode.message.domain.DirectMessage;
import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;
import com.dizzycode.dizzycode.message.domain.dto.MessageCreateDTO;
import com.dizzycode.dizzycode.message.domain.dto.MessageDetailDTO;
import com.dizzycode.dizzycode.message.infrastructure.DirectMessageRepository;
import com.dizzycode.dizzycode.member.infrastructure.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectMessageService {

    private final DirectMessageRepository directMessageRepository;
    private final MemberJpaRepository memberJpaRepository;

    public MessageDetailDTO saveDirectMessage(MessageCreateDTO messageCreateDTO, Long roomId) {
        Long senderId = messageCreateDTO.getSenderId();
        String content = messageCreateDTO.getContent();
        Optional<MemberEntity> member = memberJpaRepository.findById(senderId);

        DirectMessage directMessage = new DirectMessage();
        directMessage.setContent(content);
        directMessage.setMemberId(senderId);
        directMessage.setMemberUsername(member.orElseThrow().getUsername());
        directMessage.setUrl(messageCreateDTO.getUrl());
        directMessage.setRoomId(roomId);
        DirectMessage newDirectMessage = directMessageRepository.save(directMessage);

        MessageDetailDTO messageDetailDTO = new MessageDetailDTO();
        messageDetailDTO.setMessageId(newDirectMessage.getId());
        messageDetailDTO.setUrl(newDirectMessage.getUrl());
        messageDetailDTO.setContent(newDirectMessage.getContent());
        messageDetailDTO.setTimestamp(newDirectMessage.getCreatedAt());
        messageDetailDTO.setSenderUsername(newDirectMessage.getMemberUsername());

        return messageDetailDTO;
    }

    public List<MessageDetailDTO> messageList(Long roomId, LocalDateTime last) {
        List<MessageDetailDTO> messageList= directMessageRepository.findMessages(roomId, last).stream()
                .map(message -> {
                    MessageDetailDTO messageDetailDTO = new MessageDetailDTO();
                    messageDetailDTO.setMessageId(message.getId());
                    messageDetailDTO.setUrl(message.getUrl());
                    messageDetailDTO.setSenderUsername(message.getMemberUsername());
                    messageDetailDTO.setContent(message.getContent());
                    messageDetailDTO.setTimestamp(message.getCreatedAt());

                    return messageDetailDTO;
                })
                .collect(Collectors.toList());

        return messageList;
    }
}
