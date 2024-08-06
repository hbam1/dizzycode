package com.dizzycode.dizzycode.roommember.service;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.room.domain.Room;
import com.dizzycode.dizzycode.room.service.port.RoomRepository;
import com.dizzycode.dizzycode.roommember.domain.RoomMember;
import com.dizzycode.dizzycode.roommember.domain.RoomMemberId;
import com.dizzycode.dizzycode.roommember.domain.dto.RoomMemberDetailDTO;
import com.dizzycode.dizzycode.roommember.service.port.RoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomMemberService {

    private final RoomMemberRepository roomMemberRepository;

    // 방 입장
    @Transactional
    public RoomMemberDetailDTO roomIn(Long roomId) throws ClassNotFoundException {
        RoomMember save = roomMemberRepository.save(roomId);
        RoomMemberDetailDTO roomMemberDetailDTO = new RoomMemberDetailDTO();
        roomMemberDetailDTO.setRoomMemberId(save.getRoomMemberId());

        return roomMemberDetailDTO;
    }
}
