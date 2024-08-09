package com.dizzycode.dizzycode.room.service;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.member.service.port.MemberRepository;
import com.dizzycode.dizzycode.room.domain.Room;
import com.dizzycode.dizzycode.roommember.domain.RoomMember;
import com.dizzycode.dizzycode.roommember.domain.RoomMemberId;
import com.dizzycode.dizzycode.member.domain.MemberStatus;
import com.dizzycode.dizzycode.room.domain.room.RoomCreateDTO;
import com.dizzycode.dizzycode.room.domain.room.RoomCreateWithCCDTO;
import com.dizzycode.dizzycode.room.domain.room.RoomDetailDTO;
import com.dizzycode.dizzycode.room.domain.room.RoomRemoveDTO;
import com.dizzycode.dizzycode.member.exception.NoMemberException;
import com.dizzycode.dizzycode.room.service.port.RoomRepository;
import com.dizzycode.dizzycode.roommember.service.port.RoomMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final RedisTemplate<String, String> redisTemplate;


    // 방 생성
    @Transactional
    public RoomCreateWithCCDTO createRoom(RoomCreateDTO roomCreateDTO) {
        return roomRepository.save(roomCreateDTO);
    }

    // 방 목록
    @Transactional
    public List<RoomDetailDTO> list() {
        Member member = getMemberFromSession();

        List<RoomDetailDTO> rooms = roomMemberRepository.findRoomsByMemberId(member.getId()).stream()
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
    public RoomDetailDTO roomRetrieve(Long roomId) throws ClassNotFoundException {
        Room room = roomRepository.findByRoomId(roomId).orElseThrow(() -> new ClassNotFoundException("방이 존재하지 않습니다."));

        RoomDetailDTO roomDetailDTO = new RoomDetailDTO();
        roomDetailDTO.setRoomId(roomId);
        roomDetailDTO.setRoomName(room.getRoomName());
        roomDetailDTO.setOpen(room.isOpen());

        return roomDetailDTO;
    }

    @Transactional
    public RoomRemoveDTO roomRemove(Long roomId) throws ClassNotFoundException {
        roomRepository.delete(roomId);
        RoomRemoveDTO roomRemoveDTO = new RoomRemoveDTO();

        return roomRemoveDTO;
    }

    // 방 나가기
    @Transactional
    public boolean out(Long roomId) {
        Member member = getMemberFromSession();
        RoomMemberId roomMemberId = new RoomMemberId(member.getId(), roomId);
        RoomMember roomMember = roomMemberRepository.findRoomMemberByRoomMemberId(roomMemberId).orElseThrow();
        roomMemberRepository.delete(roomMember);

        return true;
    }

    @Transactional
    public List<MemberStatus> getRoomMembers(Long roomId) {
        List<Member> members = roomMemberRepository.findMembersByRoomId(roomId);

        // Redis pipeline을 사용하여 모든 멤버의 상태를 한 번에 가져옴
        List<Object> pipelineResults = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (Member member : members) {
                connection.hashCommands().hGet(("memberId:" + member.getId()).getBytes(), "status".getBytes());
            }

            return null;
        });

        // Redis 결과를 Map 형태로 저장
        Map<Long, String> memberStatusMap = new HashMap<>();
        for (int i = 0; i < members.size(); i++) {
            memberStatusMap.put(members.get(i).getId(), (String) pipelineResults.get(i));
        }

        // 멤버 리스트와 Redis 결과를 조합하여 MemberStatusDTO 리스트 생성
        List<MemberStatus> memberStatuses = members.stream()
                .map(member -> {
                    MemberStatus memberStatus = new MemberStatus();
                    memberStatus.setUsername(member.getUsername());

                    // Redis 결과에서 상태를 가져옴
                    String status = memberStatusMap.get(member.getId());
                    memberStatus.setStatus(status);

                    return memberStatus;
                })
                .collect(Collectors.toList());

        return memberStatuses;
    }

    private Member getMemberFromSession() {
        // 현재 인증된 사용자의 인증 객체를 가져옴
        String[] memberInfo = SecurityContextHolder.getContext().getAuthentication().getName().split(" ");
        String email = memberInfo[1];

        return memberRepository.findByEmail(email).orElseThrow(() -> new NoMemberException("존재하지 않는 회원입니다."));
    }
}
