package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.Category;
import com.dizzycode.dizzycode.domain.Channel;
import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;
import com.dizzycode.dizzycode.domain.Room;
import com.dizzycode.dizzycode.domain.roommember.RoomMember;
import com.dizzycode.dizzycode.domain.roommember.RoomMemberId;
import com.dizzycode.dizzycode.dto.RoomMemberDetailDTO;
import com.dizzycode.dizzycode.member.domain.MemberStatus;
import com.dizzycode.dizzycode.dto.room.RoomCreateDTO;
import com.dizzycode.dizzycode.dto.room.RoomCreateWithCCDTO;
import com.dizzycode.dizzycode.dto.room.RoomDetailDTO;
import com.dizzycode.dizzycode.dto.room.RoomRemoveDTO;
import com.dizzycode.dizzycode.exception.member.NoMemberException;
import com.dizzycode.dizzycode.member.infrastructure.MemberJpaRepository;
import com.dizzycode.dizzycode.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final CategoryRepository categoryRepository;
    private final ChannelRepository channelRepository;
    private final RedisTemplate<String, String> redisTemplate;


    public RoomCreateWithCCDTO createRoom(RoomCreateDTO roomCreateDTO) {
        // 현재 인증된 사용자의 인증 객체
        MemberEntity memberEntity = getMemberFromSession();

        // 방 생성
        Room room = new Room();
        room.setRoomName(roomCreateDTO.getRoomName());
        room.setOpen(roomCreateDTO.isOpen());
        room = roomRepository.save(room);

        // 방과 방 주인 설정
        RoomMember roomMember = new RoomMember();
        RoomMemberId roomMemberId = new RoomMemberId(memberEntity.getId(), room.getRoomId());
        roomMember.setRoomMemberId(roomMemberId);
        roomMember.setRoom(room);
        roomMember.setMemberEntity(memberEntity);
        roomMember.setManager(true);
        roomMemberRepository.save(roomMember);

        // 카테고리 및 채널 기본 생성 (채팅 카테고리와 음성 카테고리 기본 생성 및 각 카테고리 별 일반 채널 기본 생성)
        Category category1 = new Category();
        category1.setRoom(room);
        category1.setCategoryName("채팅 채널");
        category1 = categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setRoom(room);
        category2.setCategoryName("음성 채널");
        category2 = categoryRepository.save(category2);

        Channel channel1 = new Channel();
        channel1.setCategory(category1);
        channel1.setChannelName("일반");
        channel1.setChannelType(Channel.ChannelType.CHAT);
        channelRepository.save(channel1);

        Channel channel2 = new Channel();
        channel2.setCategory(category2);
        channel2.setChannelName("일반");
        channel2.setChannelType(Channel.ChannelType.VOICE);
        channelRepository.save(channel2);

        // 반환 DTO
        // 방 객체
        RoomCreateWithCCDTO roomCreateWithCCDTO = new RoomCreateWithCCDTO();
        roomCreateWithCCDTO.setRoomId(room.getRoomId());
        roomCreateWithCCDTO.setRoomName(room.getRoomName());

        // 채팅 채널 카테고리
        RoomCreateWithCCDTO.Category categoryDTO1 = new RoomCreateWithCCDTO.Category();
        categoryDTO1.setCategoryId(category1.getCategoryId());
        categoryDTO1.setCategoryName(category1.getCategoryName());

        // 음성 채널 카테고리
        RoomCreateWithCCDTO.Category categoryDTO2 = new RoomCreateWithCCDTO.Category();
        categoryDTO2.setCategoryId(category2.getCategoryId());
        categoryDTO2.setCategoryName(category2.getCategoryName());

        // 채팅 채널 카테고리 일반 채널
        RoomCreateWithCCDTO.Channel category1Channel = new RoomCreateWithCCDTO.Channel();
        category1Channel.setChannelId(channel1.getChannelId());
        category1Channel.setChannelName(channel1.getChannelName());
        category1Channel.setChannelType(Channel.ChannelType.CHAT);

        // 음성 채널 카테고리 일반 채널
        RoomCreateWithCCDTO.Channel category2Channel = new RoomCreateWithCCDTO.Channel();
        category2Channel.setChannelId(channel2.getChannelId());
        category2Channel.setChannelName(channel2.getChannelName());
        category2Channel.setChannelType(Channel.ChannelType.VOICE);

        // 각 카테고리에 일반 채널 리스트로 할당
        categoryDTO1.setChannels(Arrays.asList(category1Channel));
        categoryDTO2.setChannels(Arrays.asList(category2Channel));

        // 방에 카테고리 리스트로 할당
        roomCreateWithCCDTO.setCategories(Arrays.asList(categoryDTO1, categoryDTO2));

        return roomCreateWithCCDTO;
    }

    public List<RoomDetailDTO> roomList() {
        MemberEntity memberEntity = getMemberFromSession();
        if (memberEntity == null) {
            throw new NoMemberException("존재하지 않는 회원입니다.");
        }

        List<RoomDetailDTO> rooms = roomMemberRepository.findRoomsByMemberId(memberEntity.getId()).stream()
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

    public List<RoomDetailDTO> roomAll() {
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

    public RoomDetailDTO roomRetrieve(Long roomId) throws ClassNotFoundException {
        Room room = roomRepository.findByRoomId(roomId);

        if (room == null) {
            throw new ClassNotFoundException("방이 존재하지 않습니다.");
        }

        RoomDetailDTO roomDetailDTO = new RoomDetailDTO();
        roomDetailDTO.setRoomId(roomId);
        roomDetailDTO.setRoomName(room.getRoomName());
        roomDetailDTO.setOpen(room.isOpen());

        return roomDetailDTO;
    }

    public RoomRemoveDTO roomRemove(Long roomId) throws ClassNotFoundException {
        Room room = roomRepository.findByRoomId(roomId);

        if (room == null) {
            throw new ClassNotFoundException("방이 존재하지 않습니다.");
        }

        roomRepository.delete(room);
        RoomRemoveDTO roomRemoveDTO = new RoomRemoveDTO();

        return roomRemoveDTO;
    }

    public RoomMemberDetailDTO roomIn(Long roomId) {
        MemberEntity memberEntity = getMemberFromSession();
        Room room = roomRepository.findByRoomId(roomId);
        RoomMemberId roomMemberId = new RoomMemberId(memberEntity.getId(), roomId);

        RoomMember roomMember = new RoomMember();
        roomMember.setRoomMemberId(roomMemberId);
        roomMember.setRoom(room);
        roomMember.setMemberEntity(memberEntity);
        RoomMember save = roomMemberRepository.save(roomMember);

        RoomMemberDetailDTO roomMemberDetailDTO = new RoomMemberDetailDTO();
        roomMemberDetailDTO.setRoomMemberId(save.getRoomMemberId());

        return roomMemberDetailDTO;
    }

    public boolean roomOut(Long roomId) {
        MemberEntity memberEntity = getMemberFromSession();
        RoomMemberId roomMemberId = new RoomMemberId(memberEntity.getId(), roomId);
        RoomMember roomMember = roomMemberRepository.findRoomMemberByRoomMemberId(roomMemberId);
        roomMemberRepository.delete(roomMember);

        return true;
    }

    public List<MemberStatus> getRoomMembers(Long roomId) {
        List<MemberEntity> memberEntities = roomMemberRepository.findMembersByRoomId(roomId);

        // Redis pipeline을 사용하여 모든 멤버의 상태를 한 번에 가져옴
        List<Object> pipelineResults = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (MemberEntity member : memberEntities) {
                connection.hashCommands().hGet(("memberId:" + member.getId()).getBytes(), "status".getBytes());
            }

            return null;
        });

        // Redis 결과를 Map 형태로 저장
        Map<Long, String> memberStatusMap = new HashMap<>();
        for (int i = 0; i < memberEntities.size(); i++) {
            memberStatusMap.put(memberEntities.get(i).getId(), (String) pipelineResults.get(i));
        }

        // 멤버 리스트와 Redis 결과를 조합하여 MemberStatusDTO 리스트 생성
        List<MemberStatus> memberStatuses = memberEntities.stream()
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

    private MemberEntity getMemberFromSession() {
        // 현재 인증된 사용자의 인증 객체를 가져옴
        String[] memberInfo = SecurityContextHolder.getContext().getAuthentication().getName().split(" ");
        String email = memberInfo[1];

        return memberJpaRepository.findByEmail(email).orElseThrow(() -> new NoMemberException("존재하지 않는 회원입니다."));
    }
}
