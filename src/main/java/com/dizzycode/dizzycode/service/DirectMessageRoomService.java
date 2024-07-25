package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.DirectMessageRoom;
import com.dizzycode.dizzycode.domain.Member;
import com.dizzycode.dizzycode.domain.roommember.DMRoomMember;
import com.dizzycode.dizzycode.domain.roommember.RoomMember;
import com.dizzycode.dizzycode.domain.roommember.RoomMemberId;
import com.dizzycode.dizzycode.dto.member.MemberStatusDTO;
import com.dizzycode.dizzycode.dto.room.DMRoomCreateDTO;
import com.dizzycode.dizzycode.dto.room.DMRoomCreateResponseDTO;
import com.dizzycode.dizzycode.dto.room.DMRoomDetailDTO;
import com.dizzycode.dizzycode.dto.room.RoomDetailDTO;
import com.dizzycode.dizzycode.repository.DirectMessageRoomRepository;
import com.dizzycode.dizzycode.repository.DirectRoomMemberRepository;
import com.dizzycode.dizzycode.repository.MemberRepository;
import com.dizzycode.dizzycode.repository.RoomMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DirectMessageRoomService {

    private final DirectMessageRoomRepository directMessageRoomRepository;
    private final DirectRoomMemberRepository directRoomMemberRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;

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

    public List<DMRoomDetailDTO> roomList() {
        Member member = getMemberFromSession();

        List<DMRoomDetailDTO> rooms = directRoomMemberRepository.findRoomsByMemberId(member.getId()).stream()
                .map(room -> {
                    DMRoomDetailDTO roomDetailDTO = new DMRoomDetailDTO();
                    roomDetailDTO.setRoomId(room.getRoomId());
                    roomDetailDTO.setRoomName(room.getRoomName());
                    // 모든 DM room은 잠정적으로 closed 상태라고 가정
                    roomDetailDTO.setOpen(false);
                    roomDetailDTO.setMemberCount(room.getRoomMembers().size());
                    roomDetailDTO.setUserNames(getUsernamesFromSet(room.getRoomMembers(), getMemberFromSession().getUsername()));

                    if (room.getRoomName().isEmpty()) {
                        roomDetailDTO.setTemporaryRoomName(generateTemporaryName(room.getRoomMembers(), member.getUsername()));
                    }

                    return roomDetailDTO;
                })
                .collect(Collectors.toList());

        return rooms;
    }

    public DMRoomDetailDTO retrieveDMRoom(Long roomId) {
        DirectMessageRoom room = directMessageRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 방을 찾을 수 없습니다."));

        DMRoomDetailDTO roomDetailDTO = new DMRoomDetailDTO();
        roomDetailDTO.setRoomId(room.getRoomId());
        roomDetailDTO.setRoomName(room.getRoomName());
        roomDetailDTO.setOpen(false);
        roomDetailDTO.setMemberCount(room.getRoomMembers().size());
        roomDetailDTO.setUserNames(getUsernamesFromSet(room.getRoomMembers(), getMemberFromSession().getUsername()));

        if (room.getRoomName().isEmpty()) {
            roomDetailDTO.setTemporaryRoomName(generateTemporaryName(room.getRoomMembers(), getMemberFromSession().getUsername()));
        }

        return roomDetailDTO;
    }

    public void deleteDMRoom(Long roomId) {
        DirectMessageRoom room = directMessageRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 방을 찾을 수 없습니다."));

        for (DMRoomMember roomMember : room.getRoomMembers()) {
            directRoomMemberRepository.delete(roomMember);
        }

        directMessageRoomRepository.delete(room);
    }

    public void addMemberToDMRoom(Long roomId, String username) {
        DirectMessageRoom room = directMessageRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 방을 찾을 수 없습니다."));

        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }

        if (room.getRoomMembers().stream().anyMatch(dmRoomMember -> dmRoomMember.getMember().equals(member))) {
            throw new IllegalArgumentException("이미 방에 속해 있는 멤버입니다.");
        }

        DMRoomMember roomMember = new DMRoomMember();
        RoomMemberId roomMemberId = new RoomMemberId(member.getId(), room.getRoomId());
        roomMember.setMember(member);
        roomMember.setRoomMemberId(roomMemberId);
        roomMember.setRoom(room);
        directRoomMemberRepository.save(roomMember);

        room.getRoomMembers().add(roomMember);
    }

    public void removeMemberFromDMRoom(Long roomId, String username) {
        DirectMessageRoom room = directMessageRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 방을 찾을 수 없습니다."));

        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            throw new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.");
        }

        DMRoomMember targetMember = room.getRoomMembers().stream()
                .filter(dmRoomMember -> dmRoomMember.getMember().equals(member))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 방에 속해 있지 않습니다."));

        directRoomMemberRepository.delete(targetMember);

        room.getRoomMembers().remove(targetMember);
    }

    public List<MemberStatusDTO> getRoomMembers(Long roomId) {
        List<Member> members = directRoomMemberRepository.findMembersByRoomId(roomId);

        List<Object> pipelineResults = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (Member member : members) {
                connection.hashCommands().hGet(("memberId:" + member.getId()).getBytes(), "status".getBytes());
            }

            return null;
        });

        Map<Long, String> memberStatusMap = new HashMap<>();
        for (int i = 0; i < members.size(); i++) {
            memberStatusMap.put(members.get(i).getId(), (String) pipelineResults.get(i));
        }

        List<MemberStatusDTO> memberStatusDTOs = members.stream()
                .map(member -> {
                    MemberStatusDTO memberStatusDTO = new MemberStatusDTO();
                    memberStatusDTO.setUsername(member.getUsername());
                    String status = memberStatusMap.get(member.getId());
                    memberStatusDTO.setStatus(status);
                    return memberStatusDTO;
                })
                .collect(Collectors.toList());

        return memberStatusDTOs;
    }

    private Member getMemberFromSession() {
        // 현재 인증된 사용자의 인증 객체를 가져옴
        String[] memberInfo = SecurityContextHolder.getContext().getAuthentication().getName().split(" ");
        String email = memberInfo[1];

        return memberRepository.findByEmail(email);
    }

    private String generateTemporaryName(Set<DMRoomMember> roomMemberSet, String myName) {

        List<String> usernames = roomMemberSet.stream()
                .map(dmRoomMember -> dmRoomMember.getMember().getUsername())
                .filter(username -> !username.equals(myName))
                .sorted()
                .collect(Collectors.toList());
        return String.join(", ", usernames);
    }

    private List<String> getUsernamesFromSet(Set<DMRoomMember> roomMemberSet, String myName) {
        List<String> usernames = roomMemberSet.stream()
                .map(dmRoomMember -> dmRoomMember.getMember().getUsername())
                .filter(username -> !username.equals(myName))
                .sorted()
                .collect(Collectors.toList());
        return usernames;
    }
}