package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.Member;
import com.dizzycode.dizzycode.domain.Room;
import com.dizzycode.dizzycode.domain.roommember.RoomMember;
import com.dizzycode.dizzycode.domain.roommember.RoomMemberId;
import com.dizzycode.dizzycode.dto.room.RoomCreateDTO;
import com.dizzycode.dizzycode.dto.room.RoomDetailDTO;
import com.dizzycode.dizzycode.repository.MemberRepository;
import com.dizzycode.dizzycode.repository.RoomMemberRepository;
import com.dizzycode.dizzycode.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;

    public Room createRoom(RoomCreateDTO roomCreateDTO) {
        // 현재 인증된 사용자의 인증 객체를 가져옵니다.
        Member member = getMemberFromSession();

        // 방 생성
        Room room = new Room();
        room.setRoomName(roomCreateDTO.getRoomName());
        room = roomRepository.save(room);

        // 방과 방 주인 설정
        RoomMember roomMember = new RoomMember();
        RoomMemberId roomMemberId = new RoomMemberId(member.getId(), room.getRoomId());
        roomMember.setRoomMemberId(roomMemberId);
        roomMember.setRoom(room);
        roomMember.setMember(member);
        roomMember.setManager(true);
        roomMemberRepository.save(roomMember);

        return room;
    }

    public List<Room> roomList() {
        Member member = getMemberFromSession();

        List<Room> rooms = roomMemberRepository.findRoomsByMemberId(member.getId());
        return rooms;
    }

    public RoomDetailDTO roomRetrieve(Long roomId) {
        Room room = roomRepository.findByRoomId(roomId);

        RoomDetailDTO roomDetailDTO = new RoomDetailDTO();
        roomDetailDTO.setRoomId(roomId);
        roomDetailDTO.setRoomName(room.getRoomName());

        return roomDetailDTO;
    }

    private Member getMemberFromSession() {
        // 현재 인증된 사용자의 인증 객체를 가져옴
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return memberRepository.findByEmail(email);
    }
}
