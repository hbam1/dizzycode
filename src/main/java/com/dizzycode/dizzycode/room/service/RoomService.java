package com.dizzycode.dizzycode.room.service;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.member.service.port.MemberRepository;
import com.dizzycode.dizzycode.member.service.port.MemberStatusRepository;
import com.dizzycode.dizzycode.room.domain.Room;
import com.dizzycode.dizzycode.room.exception.NoRoomException;
import com.dizzycode.dizzycode.room.infrastructure.RoomIndexer;
import com.dizzycode.dizzycode.roommember.domain.RoomMember;
import com.dizzycode.dizzycode.member.domain.MemberStatus;
import com.dizzycode.dizzycode.room.domain.room.RoomCreateDTO;
import com.dizzycode.dizzycode.room.domain.room.RoomCreateWithCCDTO;
import com.dizzycode.dizzycode.room.domain.room.RoomDetailDTO;
import com.dizzycode.dizzycode.room.domain.room.RoomRemoveDTO;
import com.dizzycode.dizzycode.room.service.port.RoomRepository;
import com.dizzycode.dizzycode.roommember.service.port.RoomMemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Builder
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final MemberStatusRepository memberStatusRepository;
    private final RoomIndexer roomIndexer;

    // 방 생성
    @Transactional
    public RoomCreateWithCCDTO createRoom(RoomCreateDTO roomCreateDTO) {
        RoomCreateWithCCDTO roomCreateWithCCDTO = roomRepository.save(roomCreateDTO);
        if (roomCreateWithCCDTO.isOpen()) {
            roomIndexer.addRoomIndex(roomCreateWithCCDTO.getRoomId(), roomCreateWithCCDTO.getRoomName());
        }
        return roomCreateWithCCDTO;
    }

    // 방 목록
    @Transactional
    public List<RoomDetailDTO> list() {
        List<RoomDetailDTO> rooms = roomMemberRepository.findRoomsByMemberId().stream()
                .map(room -> {
                    RoomDetailDTO roomDetailDTO = new RoomDetailDTO();
                    roomDetailDTO.setRoomId(room.getRoomId());
                    roomDetailDTO.setRoomName(room.getRoomName());
                    roomDetailDTO.setOpen(room.isOpen());

                    return roomDetailDTO;
                })
                .collect(Collectors.toList());

        return rooms;
    }

    @Transactional
    public List<RoomDetailDTO> findAll() {
        List<RoomDetailDTO> rooms = roomRepository.findRoomsByOpenIs(true).stream()
                .map(room -> {
                    RoomDetailDTO roomDetailDTO = new RoomDetailDTO();
                    roomDetailDTO.setRoomId(room.getRoomId());
                    roomDetailDTO.setRoomName(room.getRoomName());
                    roomDetailDTO.setOpen(room.isOpen());

                    return roomDetailDTO;
                })
                .collect(Collectors.toList());

        return rooms;
    }

    @Transactional
    public RoomDetailDTO roomRetrieve(Long roomId) {
        Room room = roomRepository.findByRoomId(roomId).orElseThrow(() -> new NoRoomException("방이 존재하지 않습니다."));
        RoomDetailDTO roomDetailDTO = new RoomDetailDTO();
        roomDetailDTO.setRoomId(roomId);
        roomDetailDTO.setRoomName(room.getRoomName());
        roomDetailDTO.setOpen(room.isOpen());

        return roomDetailDTO;
    }

    @Transactional
    public RoomRemoveDTO roomRemove(Long roomId){
        roomRepository.delete(roomId);
        roomIndexer.deleteRoomIndex(roomId);
        RoomRemoveDTO roomRemoveDTO = new RoomRemoveDTO();

        return roomRemoveDTO;
    }

    // 방 나가기
    @Transactional
    public boolean out(Long roomId) {
        RoomMember roomMember = roomMemberRepository.findRoomMemberByRoomMemberId(roomId).orElseThrow();
        roomMemberRepository.delete(roomMember);

        return true;
    }

    @Transactional
    public List<MemberStatus> getRoomMembers(Long roomId) {
        List<Member> members = roomMemberRepository.findMembersByRoomId(roomId);
        List<Object> membersStatus = memberStatusRepository.membersStatus(members);

        // 결과를 Map 형태로 저장
        Map<Long, String> memberStatusMap = new HashMap<>();
        for (int i = 0; i < members.size(); i++) {
            memberStatusMap.put(members.get(i).getId(), (String) membersStatus.get(i));
        }

        // 멤버 리스트와 결과를 조합하여 MemberStatusDTO 리스트 생성
        List<MemberStatus> memberStatuses = members.stream()
                .map(member -> {
                    MemberStatus memberStatus = new MemberStatus();
                    memberStatus.setUsername(member.getUsername());

                    // 결과에서 상태를 가져옴
                    String status = memberStatusMap.get(member.getId());
                    memberStatus.setStatus(status);

                    return memberStatus;
                })
                .collect(Collectors.toList());

        return memberStatuses;
    }
}
