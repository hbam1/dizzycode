package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.DirectMessageRoom;
import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;
import com.dizzycode.dizzycode.domain.roommember.DMRoomMember;
import com.dizzycode.dizzycode.domain.roommember.RoomMemberId;
import com.dizzycode.dizzycode.member.domain.MemberStatus;
import com.dizzycode.dizzycode.dto.room.DMRoomCreateDTO;
import com.dizzycode.dizzycode.dto.room.DMRoomDetailDTO;
import com.dizzycode.dizzycode.repository.DirectMessageRoomRepository;
import com.dizzycode.dizzycode.repository.DirectRoomMemberRepository;
import com.dizzycode.dizzycode.member.infrastructure.MemberJpaRepository;
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
    private final MemberJpaRepository memberJpaRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public DMRoomDetailDTO createDMRoom(DMRoomCreateDTO dmRoomCreateDTO) {

        long memberCount = dmRoomCreateDTO.getUserNames().size();

        if (memberCount == 2) {
            DirectMessageRoom room;
            room = directMessageRoomRepository.findByFriendshipId(generateFriendshipId(dmRoomCreateDTO.getUserNames()));
            if (room == null) {
                room = new DirectMessageRoom();
                room.setRoomName(dmRoomCreateDTO.getRoomName());
                room = directMessageRoomRepository.save(room);

                Set<DMRoomMember> roomMemberSet = new HashSet<>();

                for (String username : dmRoomCreateDTO.getUserNames()) {
                    DMRoomMember roomMember = new DMRoomMember();
                    MemberEntity memberEntity = memberJpaRepository.findByUsername(username);
                    RoomMemberId roomMemberId = new RoomMemberId(memberEntity.getId(), room.getRoomId());
                    roomMember.setMemberEntity(memberEntity);
                    roomMember.setRoomMemberId(roomMemberId);
                    roomMember.setRoom(room);

                    roomMember = directRoomMemberRepository.save(roomMember);
                    roomMemberSet.add(roomMember);
                }

                room.setRoomMembers(roomMemberSet);
                room.setGroupChat(false);
                room.setFriendshipId(generateFriendshipId(dmRoomCreateDTO.getUserNames()));
            }

            DMRoomDetailDTO dmRoomDetailDTO = new DMRoomDetailDTO();
            dmRoomDetailDTO.setRoomId(room.getRoomId());
            dmRoomDetailDTO.setUserNames(getUsernamesFromSet(room.getRoomMembers(), getMemberFromSession().getUsername()));
            dmRoomDetailDTO.setMemberCount(room.getRoomMembers().size());
            dmRoomDetailDTO.setGroupChat(room.isGroupChat());
            dmRoomDetailDTO.setTemporaryRoomName(generateTemporaryName(room.getRoomMembers(), getMemberFromSession().getUsername()));
            dmRoomDetailDTO.setRoomName(room.getRoomName());
            dmRoomDetailDTO.setClose(room.isClosed());

            return dmRoomDetailDTO;
        }

        DirectMessageRoom room = new DirectMessageRoom();
        room.setRoomName(dmRoomCreateDTO.getRoomName());
        room = directMessageRoomRepository.save(room);

        Set<DMRoomMember> roomMemberSet = new HashSet<>();

        for (String username : dmRoomCreateDTO.getUserNames()) {
            DMRoomMember roomMember = new DMRoomMember();
            MemberEntity memberEntity = memberJpaRepository.findByUsername(username);
            RoomMemberId roomMemberId = new RoomMemberId(memberEntity.getId(), room.getRoomId());
            roomMember.setMemberEntity(memberEntity);
            roomMember.setRoomMemberId(roomMemberId);
            roomMember.setRoom(room);

            roomMember = directRoomMemberRepository.save(roomMember);
            roomMemberSet.add(roomMember);
        }

        room.setRoomMembers(roomMemberSet);
        room.setGroupChat(true);
        room.setRoomName("");
        room.setFriendshipId("");

        DMRoomDetailDTO dmRoomDetailDTO = new DMRoomDetailDTO();
        dmRoomDetailDTO.setRoomId(room.getRoomId());
        dmRoomDetailDTO.setUserNames(getUsernamesFromSet(room.getRoomMembers(), getMemberFromSession().getUsername()));
        dmRoomDetailDTO.setMemberCount(room.getRoomMembers().size());
        dmRoomDetailDTO.setGroupChat(room.isGroupChat());
        dmRoomDetailDTO.setTemporaryRoomName(generateTemporaryName(room.getRoomMembers(), getMemberFromSession().getUsername()));
        dmRoomDetailDTO.setRoomName(room.getRoomName());
        dmRoomDetailDTO.setClose(room.isClosed());

        return dmRoomDetailDTO;
    }

    public List<DMRoomDetailDTO> roomList() {
        MemberEntity memberEntity = getMemberFromSession();

        List<DMRoomDetailDTO> rooms = directRoomMemberRepository.findRoomsByMemberId(memberEntity.getId()).stream()
                .map(room -> {
                    DMRoomDetailDTO roomDetailDTO = new DMRoomDetailDTO();
                    roomDetailDTO.setRoomId(room.getRoomId());
                    roomDetailDTO.setRoomName(room.getRoomName());
                    // 모든 DM room은 잠정적으로 closed 상태라고 가정
                    roomDetailDTO.setOpen(false);
                    roomDetailDTO.setClose(room.isClosed());
                    roomDetailDTO.setGroupChat(room.isGroupChat());
                    roomDetailDTO.setMemberCount(room.getRoomMembers().size());
                    roomDetailDTO.setUserNames(getUsernamesFromSet(room.getRoomMembers(), getMemberFromSession().getUsername()));

                    if (room.getRoomName().isEmpty()) {
                        roomDetailDTO.setTemporaryRoomName(generateTemporaryName(room.getRoomMembers(), memberEntity.getUsername()));
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
        roomDetailDTO.setClose(room.isClosed());
        roomDetailDTO.setGroupChat(room.isGroupChat());

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

    public DMRoomDetailDTO addMemberToDMRoom(Long roomId, String username) {
        DirectMessageRoom room = directMessageRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 방을 찾을 수 없습니다."));

        MemberEntity memberEntity = memberJpaRepository.findByUsername(username);
        if (memberEntity == null) {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }

        if (room.getRoomMembers().stream().anyMatch(dmRoomMember -> dmRoomMember.getMemberEntity().equals(memberEntity))) {
            throw new IllegalArgumentException("이미 방에 속해 있는 멤버입니다.");
        }

        if (!room.isGroupChat()) {
            List<String> usernames = room.getRoomMembers().stream().map(user ->
                user.getMemberEntity().getUsername()).collect(Collectors.toCollection(ArrayList::new));
            usernames.add(username);

            DMRoomCreateDTO dmRoomCreateDTO = new DMRoomCreateDTO();
            dmRoomCreateDTO.setRoomName("");
            dmRoomCreateDTO.setUserNames(usernames);
            DMRoomDetailDTO dmRoomDetailDTO = createDMRoom(dmRoomCreateDTO);

            return dmRoomDetailDTO;

        } else {
            DMRoomMember roomMember = new DMRoomMember();
            RoomMemberId roomMemberId = new RoomMemberId(memberEntity.getId(), room.getRoomId());
            roomMember.setMemberEntity(memberEntity);
            roomMember.setRoomMemberId(roomMemberId);
            roomMember.setRoom(room);
            directRoomMemberRepository.save(roomMember);

            room.getRoomMembers().add(roomMember);
            room.setGroupChat(true);

            DMRoomDetailDTO dmRoomDetailDTO = new DMRoomDetailDTO();
            dmRoomDetailDTO.setUserNames(getUsernamesFromSet(room.getRoomMembers(), getMemberFromSession().getUsername()));
            dmRoomDetailDTO.setMemberCount(room.getRoomMembers().size());
            dmRoomDetailDTO.setGroupChat(room.isGroupChat());
            dmRoomDetailDTO.setTemporaryRoomName(generateTemporaryName(room.getRoomMembers(), getMemberFromSession().getUsername()));
            dmRoomDetailDTO.setRoomName(room.getRoomName());
            dmRoomDetailDTO.setClose(room.isClosed());

            return dmRoomDetailDTO;
        }
    }

    public void removeMemberFromDMRoom(Long roomId, String username) {
        DirectMessageRoom room = directMessageRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 방을 찾을 수 없습니다."));

        MemberEntity memberEntity = memberJpaRepository.findByUsername(username);
        if (memberEntity == null) {
            throw new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.");
        }

        DMRoomMember targetMember = room.getRoomMembers().stream()
                .filter(dmRoomMember -> dmRoomMember.getMemberEntity().equals(memberEntity))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 방에 속해 있지 않습니다."));

        directRoomMemberRepository.delete(targetMember);

        room.getRoomMembers().remove(targetMember);
    }

    public List<MemberStatus> getRoomMembers(Long roomId) {
        List<MemberEntity> memberEntities = directRoomMemberRepository.findMembersByRoomId(roomId);

        List<Object> pipelineResults = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (MemberEntity member : memberEntities) {
                connection.hashCommands().hGet(("memberId:" + member.getId()).getBytes(), "status".getBytes());
            }

            return null;
        });

        Map<Long, String> memberStatusMap = new HashMap<>();
        for (int i = 0; i < memberEntities.size(); i++) {
            memberStatusMap.put(memberEntities.get(i).getId(), (String) pipelineResults.get(i));
        }

        List<MemberStatus> memberStatuses = memberEntities.stream()
                .map(member -> {
                    MemberStatus memberStatus = new MemberStatus();
                    memberStatus.setUsername(member.getUsername());
                    String status = memberStatusMap.get(member.getId());
                    memberStatus.setStatus(status);
                    return memberStatus;
                })
                .collect(Collectors.toList());

        return memberStatuses;
    }

    private String generateFriendshipId(List<String> usernames) {
        String friendshipId = usernames.stream().map(username ->
            memberJpaRepository.findByUsername(username).getId()).sorted(Comparator.naturalOrder()).map(String::valueOf).collect(Collectors.joining("-"));

        return friendshipId;
    }

    private MemberEntity getMemberFromSession() {
        // 현재 인증된 사용자의 인증 객체를 가져옴
        String[] memberInfo = SecurityContextHolder.getContext().getAuthentication().getName().split(" ");
        String email = memberInfo[1];

        return memberJpaRepository.findByEmail(email);
    }

    private String generateTemporaryName(Set<DMRoomMember> roomMemberSet, String myName) {

        List<String> usernames = roomMemberSet.stream()
                .map(dmRoomMember -> dmRoomMember.getMemberEntity().getUsername())
                .filter(username -> !username.equals(myName))
                .sorted()
                .collect(Collectors.toList());
        return String.join(", ", usernames);
    }

    private List<String> getUsernamesFromSet(Set<DMRoomMember> roomMemberSet, String myName) {
        List<String> usernames = roomMemberSet.stream()
                .map(dmRoomMember -> dmRoomMember.getMemberEntity().getUsername())
                .filter(username -> !username.equals(myName))
                .sorted()
                .collect(Collectors.toList());
        return usernames;
    }
}