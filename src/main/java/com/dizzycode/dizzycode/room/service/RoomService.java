package com.dizzycode.dizzycode.room.service;

import com.dizzycode.dizzycode.category.domain.Category;
import com.dizzycode.dizzycode.category.service.port.CategoryRepository;
import com.dizzycode.dizzycode.channel.domain.Channel;
import com.dizzycode.dizzycode.channel.domain.ChannelType;
import com.dizzycode.dizzycode.channel.service.port.ChannelRepository;
import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.member.service.port.MemberRepository;
import com.dizzycode.dizzycode.room.domain.Room;
import com.dizzycode.dizzycode.roommember.domain.RoomMember;
import com.dizzycode.dizzycode.roommember.domain.RoomMemberId;
import com.dizzycode.dizzycode.roommember.domain.dto.RoomMemberDetailDTO;
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

import java.util.Arrays;
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
    private final CategoryRepository categoryRepository;
    private final ChannelRepository channelRepository;
    private final RedisTemplate<String, String> redisTemplate;


    // 방 생성
    @Transactional
    public RoomCreateWithCCDTO createRoom(RoomCreateDTO roomCreateDTO) {
        // 현재 인증된 사용자의 인증 객체
        Member member = getMemberFromSession();

        // 방 생성
        Room room = Room.builder()
                .roomName(roomCreateDTO.getRoomName())
                .open(roomCreateDTO.isOpen())
                .build();
        room = roomRepository.save(room);

        // 방과 방 주인 설정
        RoomMemberId roomMemberId = new RoomMemberId(member.getId(), room.getRoomId());
        RoomMember roomMember = RoomMember.builder()
                .roomMemberId(roomMemberId)
                .room(room)
                .member(member)
                .manager(true)
                .build();
        roomMemberRepository.save(roomMember);

        // 카테고리 및 채널 기본 생성 (채팅 카테고리와 음성 카테고리 기본 생성 및 각 카테고리 별 일반 채널 기본 생성)
        Category category1 = Category.builder()
                .room(room)
                .categoryName("채팅 채널")
                .build();
        category1 = categoryRepository.save(category1);

        Category category2 = Category.builder()
                .room(room)
                .categoryName("음성 채널")
                .build();
        category2 = categoryRepository.save(category2);

        Channel channelEntity1 = Channel.builder()
                .category(category1)
                .channelName("일반")
                .channelType(ChannelType.CHAT)
                .build();
        channelRepository.save(channelEntity1);

        Channel channelEntity2 = Channel.builder()
                .category(category2)
                .channelName("일반")
                .channelType(ChannelType.VOICE)
                .build();
        channelRepository.save(channelEntity2);

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
        category1Channel.setChannelId(channelEntity1.getChannelId());
        category1Channel.setChannelName(channelEntity1.getChannelName());
        category1Channel.setChannelType(ChannelType.CHAT);

        // 음성 채널 카테고리 일반 채널
        RoomCreateWithCCDTO.Channel category2Channel = new RoomCreateWithCCDTO.Channel();
        category2Channel.setChannelId(channelEntity2.getChannelId());
        category2Channel.setChannelName(channelEntity2.getChannelName());
        category2Channel.setChannelType(ChannelType.VOICE);

        // 각 카테고리에 일반 채널 리스트로 할당
        categoryDTO1.setChannels(Arrays.asList(category1Channel));
        categoryDTO2.setChannels(Arrays.asList(category2Channel));

        // 방에 카테고리 리스트로 할당
        roomCreateWithCCDTO.setCategories(Arrays.asList(categoryDTO1, categoryDTO2));

        return roomCreateWithCCDTO;
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
        Room room = roomRepository.findByRoomId(roomId).orElseThrow(() -> new ClassNotFoundException("방이 존재하지 않습니다."));

        roomRepository.delete(room);
        RoomRemoveDTO roomRemoveDTO = new RoomRemoveDTO();

        return roomRemoveDTO;
    }

    // 방 입장
    @Transactional
    public RoomMemberDetailDTO roomIn(Long roomId) throws ClassNotFoundException {
        Member member = getMemberFromSession();
        Room room = roomRepository.findByRoomId(roomId).orElseThrow(() -> new ClassNotFoundException("방이 존재하지 않습니다."));
        RoomMemberId roomMemberId = new RoomMemberId(member.getId(), roomId);

        RoomMember roomMember = RoomMember.builder()
                .roomMemberId(roomMemberId)
                .room(room)
                .member(member)
                .build();
        RoomMember save = roomMemberRepository.save(roomMember);

        RoomMemberDetailDTO roomMemberDetailDTO = new RoomMemberDetailDTO();
        roomMemberDetailDTO.setRoomMemberId(save.getRoomMemberId());

        return roomMemberDetailDTO;
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
