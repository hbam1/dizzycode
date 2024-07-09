package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.DirectMessageRoom;
import com.dizzycode.dizzycode.domain.Member;
import com.dizzycode.dizzycode.domain.roommember.DMRoomMember;
import com.dizzycode.dizzycode.domain.roommember.RoomMember;
import com.dizzycode.dizzycode.domain.roommember.RoomMemberId;
import com.dizzycode.dizzycode.dto.room.DMRoomCreateDTO;
import com.dizzycode.dizzycode.dto.room.DMRoomCreateResponseDTO;
import com.dizzycode.dizzycode.dto.room.RoomDetailDTO;
import com.dizzycode.dizzycode.repository.DirectMessageRoomRepository;
import com.dizzycode.dizzycode.repository.DirectRoomMemberRepository;
import com.dizzycode.dizzycode.repository.MemberRepository;
import com.dizzycode.dizzycode.repository.RoomMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DirectMessageRoomService {

    private final DirectMessageRoomRepository directMessageRoomRepository;
    private final DirectRoomMemberRepository directRoomMemberRepository;
    private final MemberRepository memberRepository;

    public DMRoomCreateResponseDTO createDMRoom(DMRoomCreateDTO dmRoomCreateDTO) {

        DirectMessageRoom room = new DirectMessageRoom();
        room.setRoomName(dmRoomCreateDTO.getRoomName());
        room = directMessageRoomRepository.save(room);

        Set<DMRoomMember> roomMemberSet = new HashSet<>();

        for (String username : dmRoomCreateDTO.getUserNames()) {
            DMRoomMember roomMember = new DMRoomMember();
            Member memberEntity = memberRepository.findByUsername(username);
            RoomMemberId roomMemberId = new RoomMemberId(memberEntity.getId(), room.getRoomId());
            roomMember.setMember(memberEntity);
            roomMember.setRoomMemberId(roomMemberId);
            roomMember.setRoom(room);

            roomMember = directRoomMemberRepository.save(roomMember);
            roomMemberSet.add(roomMember);
        }

        room.setRoomMembers(roomMemberSet);

        DMRoomCreateResponseDTO dmRoomCreateResponseDTO = new DMRoomCreateResponseDTO();
        dmRoomCreateResponseDTO.setRoomId(room.getRoomId());
        dmRoomCreateResponseDTO.setRoomName(room.getRoomName());

        return dmRoomCreateResponseDTO;
    }

    public List<RoomDetailDTO> roomList() {
        Member member = getMemberFromSession();

        List<RoomDetailDTO> rooms = directRoomMemberRepository.findRoomsByMemberId(member.getId()).stream()
                .map(room -> {
                    RoomDetailDTO roomDetailDTO = new RoomDetailDTO();
                    roomDetailDTO.setRoomId(room.getRoomId());
                    roomDetailDTO.setRoomName(room.getRoomName());
                    // 모든 DM room은 잠정적으로 closed 상태라고 가정
                    roomDetailDTO.setOpen(false);

                    return roomDetailDTO;
                })
                .collect(Collectors.toList());

        return rooms;
    }

    public List<RoomDetailDTO> roomListForTest(Long memberId) {
        Member member = memberRepository.findById(memberId).get();

        List<RoomDetailDTO> rooms = directRoomMemberRepository.findRoomsByMemberId(member.getId()).stream()
                .map(room -> {
                    RoomDetailDTO roomDetailDTO = new RoomDetailDTO();
                    roomDetailDTO.setRoomId(room.getRoomId());
                    roomDetailDTO.setRoomName(room.getRoomName());
                    // 모든 DM room은 잠정적으로 closed 상태라고 가정
                    roomDetailDTO.setOpen(false);

                    return roomDetailDTO;
                })
                .collect(Collectors.toList());

        return rooms;
    }

    private Member getMemberFromSession() {
        // 현재 인증된 사용자의 인증 객체를 가져옴
        String[] memberInfo = SecurityContextHolder.getContext().getAuthentication().getName().split(" ");
        String email = memberInfo[1];

        return memberRepository.findByEmail(email);
    }
}
