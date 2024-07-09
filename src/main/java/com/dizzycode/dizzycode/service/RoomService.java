package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.Category;
import com.dizzycode.dizzycode.domain.Channel;
import com.dizzycode.dizzycode.domain.Member;
import com.dizzycode.dizzycode.domain.Room;
import com.dizzycode.dizzycode.domain.roommember.RoomMember;
import com.dizzycode.dizzycode.domain.roommember.RoomMemberId;
import com.dizzycode.dizzycode.dto.RoomMemberDetailDTO;
import com.dizzycode.dizzycode.dto.room.RoomCreateDTO;
import com.dizzycode.dizzycode.dto.room.RoomCreateWithCCDTO;
import com.dizzycode.dizzycode.dto.room.RoomDetailDTO;
import com.dizzycode.dizzycode.dto.room.RoomRemoveDTO;
import com.dizzycode.dizzycode.exception.member.NoMemberException;
import com.dizzycode.dizzycode.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final CategoryRepository categoryRepository;
    private final ChannelRepository channelRepository;

    public RoomCreateWithCCDTO createRoom(RoomCreateDTO roomCreateDTO) {
        // 현재 인증된 사용자의 인증 객체
        Member member = getMemberFromSession();

        // 방 생성
        Room room = new Room();
        room.setRoomName(roomCreateDTO.getRoomName());
        room.setOpen(roomCreateDTO.isOpen());
        room = roomRepository.save(room);

        // 방과 방 주인 설정
        RoomMember roomMember = new RoomMember();
        RoomMemberId roomMemberId = new RoomMemberId(member.getId(), room.getRoomId());
        roomMember.setRoomMemberId(roomMemberId);
        roomMember.setRoom(room);
        roomMember.setMember(member);
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
        Member member = getMemberFromSession();
        if (member == null) {
            throw new NoMemberException("존재하지 않는 회원입니다");
        }

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
        Member member = getMemberFromSession();
        Room room = roomRepository.findByRoomId(roomId);
        RoomMemberId roomMemberId = new RoomMemberId(member.getId(), roomId);

        RoomMember roomMember = new RoomMember();
        roomMember.setRoomMemberId(roomMemberId);
        roomMember.setRoom(room);
        roomMember.setMember(member);
        RoomMember save = roomMemberRepository.save(roomMember);

        RoomMemberDetailDTO roomMemberDetailDTO = new RoomMemberDetailDTO();
        roomMemberDetailDTO.setRoomMemberId(save.getRoomMemberId());

        return roomMemberDetailDTO;
    }

    public boolean roomOut(Long roomId) {
        Member member = getMemberFromSession();
        RoomMemberId roomMemberId = new RoomMemberId(member.getId(), roomId);
        RoomMember roomMember = roomMemberRepository.findRoomMemberByRoomMemberId(roomMemberId);
        roomMemberRepository.delete(roomMember);

        return true;
    }

    private Member getMemberFromSession() {
        // 현재 인증된 사용자의 인증 객체를 가져옴
        String[] memberInfo = SecurityContextHolder.getContext().getAuthentication().getName().split(" ");
        String email = memberInfo[1];

        return memberRepository.findByEmail(email);
    }
}
