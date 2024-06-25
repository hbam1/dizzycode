package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.Member;
import com.dizzycode.dizzycode.domain.Room;
import com.dizzycode.dizzycode.domain.roommember.RoomMember;
import com.dizzycode.dizzycode.domain.roommember.RoomMemberId;
import com.dizzycode.dizzycode.dto.room.RoomCreateDTO;
import com.dizzycode.dizzycode.dto.room.RoomDetailDTO;
import com.dizzycode.dizzycode.dto.room.RoomRemoveDTO;
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

    public RoomRemoveDTO roomRemove(Long roomId) throws ClassNotFoundException {
        Room room = roomRepository.findByRoomId(roomId);

        if (room == null) {
            throw new ClassNotFoundException("방이 존재하지 않습니다.");
        }

        roomRepository.delete(room);
        RoomRemoveDTO roomRemoveDTO = new RoomRemoveDTO();
        roomRemoveDTO.setMessage("방을 삭제했습니다");
        return roomRemoveDTO;
    }

    private Member getMemberFromSession() {
        // 현재 인증된 사용자의 인증 객체를 가져옴
        String[] memberInfo = SecurityContextHolder.getContext().getAuthentication().getName().split(" ");
        String email = memberInfo[1];

        return memberRepository.findByEmail(email);
    }
}
