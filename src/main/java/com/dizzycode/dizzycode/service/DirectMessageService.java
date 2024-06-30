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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DirectMessageService {

    private final DirectMessageRepository directMessageRepository;
    private final MemberRepository memberRepository;

    public DirectMessageDetailDTO saveDirectMessage(MessageCreateDTO messageCreateDTO, Long receiverId) {
        Long senderId = messageCreateDTO.getSenderId();
        String content = messageCreateDTO.getContent();
        Optional<Member> member = memberRepository.findById(senderId);

        DirectMessage directMessage = new DirectMessage();
        directMessage.setContent(content);
        directMessage.setMemberId(senderId);
        directMessage.setFriendshipId(StringifyFriendshipId(senderId, receiverId));
        DirectMessage newDirectMessage = directMessageRepository.save(directMessage);

        DirectMessageDetailDTO messageDetailDTO = new DirectMessageDetailDTO();
        messageDetailDTO.setContent(newDirectMessage.getContent());
        messageDetailDTO.setTimestamp(newDirectMessage.getCreatedAt());
        messageDetailDTO.setSenderUsername(member.orElseThrow().getUsername());
        messageDetailDTO.setFriendshipId(newDirectMessage.getFriendshipId());

        return messageDetailDTO;
    }

    private String StringifyFriendshipId(Long memberId1, Long memberId2) {
        String id;
        if (memberId1 < memberId2) {
            id = memberId1.toString() + "-" + memberId2.toString();
        } else {
            id = memberId2.toString() + "-" + memberId1.toString();
        }
        return id;
    }
}
