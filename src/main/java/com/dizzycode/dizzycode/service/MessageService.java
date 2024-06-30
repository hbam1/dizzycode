package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.Member;
import com.dizzycode.dizzycode.domain.Message;
import com.dizzycode.dizzycode.dto.message.MessageCreateDTO;
import com.dizzycode.dizzycode.dto.message.MessageDetailDTO;
import com.dizzycode.dizzycode.repository.MemberRepository;
import com.dizzycode.dizzycode.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    public MessageDetailDTO saveMessage(MessageCreateDTO messageCreateDTO, Long roomId, Long categoryId, Long channelId) {
        Message message = new Message();

        // 로그인한 사용자 확인
        Optional<Member> member = memberRepository.findById(messageCreateDTO.getSenderId());

        // 메시지 생성
        message.setMemberId(member.orElseThrow().getId());
        message.setRoomId(roomId);
        message.setCategoryId(categoryId);
        message.setChannelId(channelId);
        message.setContent(messageCreateDTO.getContent());
        message.setImageUrl("");
        Message newMessage = messageRepository.save(message);

        log.info("messageId={}", newMessage.getMessageId());

        // 메시지 디테일 반환 dto
        MessageDetailDTO messageDetailDTO = new MessageDetailDTO();
        messageDetailDTO.setContent(newMessage.getContent());
        messageDetailDTO.setSenderUsername(member.orElseThrow().getUsername());
        messageDetailDTO.setTimestamp(message.getCreatedAt());

        return messageDetailDTO;
    }
}
