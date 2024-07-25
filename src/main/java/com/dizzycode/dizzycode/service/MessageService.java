package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.Member;
import com.dizzycode.dizzycode.domain.Message;
import com.dizzycode.dizzycode.domain.roommember.RoomMember;
import com.dizzycode.dizzycode.domain.roommember.RoomMemberId;
import com.dizzycode.dizzycode.dto.message.MessageCreateDTO;
import com.dizzycode.dizzycode.dto.message.MessageDetailDTO;
import com.dizzycode.dizzycode.repository.MemberRepository;
import com.dizzycode.dizzycode.repository.MessageRepository;
import com.dizzycode.dizzycode.repository.RoomMemberRepository;
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
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;

    public MessageDetailDTO saveMessage(MessageCreateDTO messageCreateDTO, Long roomId, Long categoryId, Long channelId) {
        Message message = new Message();

        // 로그인한 사용자 확인
        Optional<Member> member = memberRepository.findById(messageCreateDTO.getSenderId());

        // 메시지 생성
        message.setMemberId(member.orElseThrow().getId());
        message.setMemberUsername(member.orElseThrow().getUsername());
        message.setRoomId(roomId);
        message.setCategoryId(categoryId);
        message.setChannelId(channelId);
        message.setContent(messageCreateDTO.getContent());
        message.setImageUrl(messageCreateDTO.getUrl());
        Message newMessage = messageRepository.save(message);

        log.info("messageId={}", newMessage.getId());

        // 메시지 디테일 반환 dto
        MessageDetailDTO messageDetailDTO = new MessageDetailDTO();
        messageDetailDTO.setMessageId(newMessage.getId());
        messageDetailDTO.setContent(newMessage.getContent());
        messageDetailDTO.setSenderUsername(member.orElseThrow().getUsername());
        messageDetailDTO.setTimestamp(newMessage.getCreatedAt());

        return messageDetailDTO;
    }

    public List<MessageDetailDTO> messageList(Long channelId, LocalDateTime last, Long roomId) {
        Member member = getMemberFromSession();
        RoomMemberId roomMemberId = new RoomMemberId(member.getId(), roomId);
        Optional<RoomMember> roomMember = roomMemberRepository.findById(roomMemberId);

        List<MessageDetailDTO> messageList= messageRepository.findMessages(channelId, last, roomMember.orElseThrow().getCreatedAt()).stream()
                .map(message -> {
                    MessageDetailDTO messageDetailDTO = new MessageDetailDTO();
                    messageDetailDTO.setMessageId(message.getId());
                    messageDetailDTO.setSenderUsername(message.getMemberUsername());
                    messageDetailDTO.setContent(message.getContent());
                    messageDetailDTO.setTimestamp(message.getCreatedAt());

                    return messageDetailDTO;
                })
                .collect(Collectors.toList());

        return messageList;
    }

    private Member getMemberFromSession() {
        // 현재 인증된 사용자의 인증 객체를 가져옴
        String[] memberInfo = SecurityContextHolder.getContext().getAuthentication().getName().split(" ");
        String email = memberInfo[1];

        return memberRepository.findByEmail(email);
    }
}
