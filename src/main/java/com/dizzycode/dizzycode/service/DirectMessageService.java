package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.DirectMessage;
import com.dizzycode.dizzycode.domain.Member;
import com.dizzycode.dizzycode.dto.message.DirectMessageDetailDTO;
import com.dizzycode.dizzycode.dto.message.MessageCreateDTO;
import com.dizzycode.dizzycode.dto.message.MessageDetailDTO;
import com.dizzycode.dizzycode.repository.DirectMessageRepository;
import com.dizzycode.dizzycode.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectMessageService {

    private final DirectMessageRepository directMessageRepository;
    private final MemberRepository memberRepository;

    public MessageDetailDTO saveDirectMessage(MessageCreateDTO messageCreateDTO, Long roomId) {
        Long senderId = messageCreateDTO.getSenderId();
        String content = messageCreateDTO.getContent();
        Optional<Member> member = memberRepository.findById(senderId);

        DirectMessage directMessage = new DirectMessage();
        directMessage.setContent(content);
        directMessage.setMemberId(senderId);
        directMessage.setMemberUsername(member.orElseThrow().getUsername());
        directMessage.setImageUrl("");
        directMessage.setRoomId(roomId);
        DirectMessage newDirectMessage = directMessageRepository.save(directMessage);

        MessageDetailDTO messageDetailDTO = new MessageDetailDTO();
        messageDetailDTO.setContent(newDirectMessage.getContent());
        messageDetailDTO.setTimestamp(newDirectMessage.getCreatedAt());
        messageDetailDTO.setSenderUsername(newDirectMessage.getMemberUsername());

        return messageDetailDTO;
    }

    public List<MessageDetailDTO> messageList(Long roomId, LocalDateTime last) {
        List<MessageDetailDTO> messageList= directMessageRepository.findMessages(roomId, last).stream()
                .map(message -> {
                    MessageDetailDTO messageDetailDTO = new MessageDetailDTO();
                    messageDetailDTO.setMessageId(message.getMessageId());
                    messageDetailDTO.setSenderUsername(message.getMemberUsername());
                    messageDetailDTO.setContent(message.getContent());
                    messageDetailDTO.setTimestamp(message.getCreatedAt());

                    return messageDetailDTO;
                })
                .collect(Collectors.toList());

        return messageList;
    }
}
