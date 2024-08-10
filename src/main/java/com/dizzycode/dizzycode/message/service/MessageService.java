package com.dizzycode.dizzycode.message.service;

import com.dizzycode.dizzycode.member.exception.NoMemberException;
import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;
import com.dizzycode.dizzycode.message.domain.RoomMessage;
import com.dizzycode.dizzycode.roommember.infrastructure.RoomMemberEntity;
import com.dizzycode.dizzycode.roommember.infrastructure.RoomMemberIdEntity;
import com.dizzycode.dizzycode.message.domain.dto.MessageCreateDTO;
import com.dizzycode.dizzycode.message.domain.dto.MessageDetailDTO;
import com.dizzycode.dizzycode.member.infrastructure.MemberJpaRepository;
import com.dizzycode.dizzycode.message.infrastructure.MessageRepository;
import com.dizzycode.dizzycode.roommember.infrastructure.RoomMemberJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final RoomMemberJpaRepository roomMemberJpaRepository;

    public MessageDetailDTO saveMessage(MessageCreateDTO messageCreateDTO, Long roomId, Long categoryId, Long channelId) {
        RoomMessage roomMessage = new RoomMessage();

        // 로그인한 사용자 확인
        Optional<MemberEntity> member = memberJpaRepository.findById(messageCreateDTO.getSenderId());

        // 메시지 생성
        roomMessage.setMemberId(member.orElseThrow().getId());
        roomMessage.setMemberUsername(member.orElseThrow().getUsername());
        roomMessage.setRoomId(roomId);
        roomMessage.setCategoryId(categoryId);
        roomMessage.setChannelId(channelId);
        roomMessage.setContent(messageCreateDTO.getContent());
        roomMessage.setUrl(messageCreateDTO.getUrl());
        RoomMessage newRoomMessage = messageRepository.save(roomMessage);

        log.info("messageId={}", newRoomMessage.getId());

        // 메시지 디테일 반환 dto
        MessageDetailDTO messageDetailDTO = new MessageDetailDTO();
        messageDetailDTO.setMessageId(newRoomMessage.getId());
        messageDetailDTO.setUrl(newRoomMessage.getUrl());
        messageDetailDTO.setContent(newRoomMessage.getContent());
        messageDetailDTO.setSenderUsername(member.orElseThrow().getUsername());
        messageDetailDTO.setTimestamp(newRoomMessage.getCreatedAt());

        return messageDetailDTO;
    }

    public List<MessageDetailDTO> messageList(Long channelId, LocalDateTime last, Long roomId) {
        MemberEntity memberEntity = getMemberFromSession();
        RoomMemberIdEntity roomMemberIdEntity = new RoomMemberIdEntity(memberEntity.getId(), roomId);
        Optional<RoomMemberEntity> roomMember = roomMemberJpaRepository.findById(roomMemberIdEntity);

        List<MessageDetailDTO> messageList= messageRepository.findMessages(channelId, last, roomMember.orElseThrow().getCreatedAt()).stream()
                .map(roomMessage -> {
                    MessageDetailDTO messageDetailDTO = new MessageDetailDTO();
                    messageDetailDTO.setMessageId(roomMessage.getId());
                    messageDetailDTO.setSenderUsername(roomMessage.getMemberUsername());
                    messageDetailDTO.setContent(roomMessage.getContent());
                    messageDetailDTO.setTimestamp(roomMessage.getCreatedAt());

                    return messageDetailDTO;
                })
                .collect(Collectors.toList());

        return messageList;
    }

    private MemberEntity getMemberFromSession() {
        // 현재 인증된 사용자의 인증 객체를 가져옴
        String[] memberInfo = SecurityContextHolder.getContext().getAuthentication().getName().split(" ");
        String email = memberInfo[1];

        return memberJpaRepository.findByEmail(email).orElseThrow(() -> new NoMemberException("존재하지 않는 회원입니다."));
    }
}
