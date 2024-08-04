package com.dizzycode.dizzycode.room.infrastructure;

import com.dizzycode.dizzycode.category.domain.Category;
import com.dizzycode.dizzycode.category.infrastructure.CategoryEntity;
import com.dizzycode.dizzycode.category.infrastructure.CategoryJpaRepository;
import com.dizzycode.dizzycode.channel.domain.ChannelType;
import com.dizzycode.dizzycode.channel.infrastructure.ChannelEntity;
import com.dizzycode.dizzycode.channel.infrastructure.ChannelJpaRepository;
import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.member.exception.NoMemberException;
import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;
import com.dizzycode.dizzycode.member.infrastructure.MemberJpaRepository;
import com.dizzycode.dizzycode.room.domain.Room;
import com.dizzycode.dizzycode.room.domain.room.RoomCreateDTO;
import com.dizzycode.dizzycode.room.domain.room.RoomCreateWithCCDTO;
import com.dizzycode.dizzycode.room.service.port.RoomRepository;
import com.dizzycode.dizzycode.roommember.domain.RoomMemberId;
import com.dizzycode.dizzycode.roommember.infrastructure.RoomMemberEntity;
import com.dizzycode.dizzycode.roommember.infrastructure.RoomMemberIdEntity;
import com.dizzycode.dizzycode.roommember.infrastructure.RoomMemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {

    private final RoomJpaRepository roomJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final RoomMemberJpaRepository roomMemberJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final ChannelJpaRepository channelJpaRepository;

    @Override
    public Optional<Room> findByRoomId(Long roomId) {
        return roomJpaRepository.findByRoomId(roomId).map(RoomEntity::toModel);
    }

    @Override
    public List<Room> findRoomsByOpenIs(boolean open) {
        return roomJpaRepository.findRoomsByOpenIs(open).stream()
                .map(roomEntity -> {
                    Room room = roomEntity.toModel();

                    return room;
                })
                .toList();
    }

    @Override
    public RoomCreateWithCCDTO save(RoomCreateDTO roomCreateDTO) {
        // 현재 인증된 사용자의 인증 객체
        MemberEntity member = getMemberFromSession();
        // 방 생성
        RoomEntity room = new RoomEntity();
        room.setRoomName(roomCreateDTO.getRoomName());
        room.setOpen(roomCreateDTO.isOpen());
        room = roomJpaRepository.save(room);
        // 방과 방 주인 설정
        RoomMemberIdEntity roomMemberId = new RoomMemberIdEntity(member.getId(), room.getRoomId());
        RoomMemberEntity roomMember = new RoomMemberEntity();
        roomMember.setRoomMemberId(roomMemberId);
        roomMember.setRoom(room);
        roomMember.setMember(member);
        roomMember.setManager(true);
        roomMemberJpaRepository.save(roomMember);
        // 카테고리 및 채널 기본 생성 (채팅 카테고리와 음성 카테고리 기본 생성 및 각 카테고리 별 일반 채널 기본 생성)
        CategoryEntity category1 = new CategoryEntity();
        category1.setRoom(room);
        category1.setCategoryName("채팅 채널");
        category1 = categoryJpaRepository.save(category1);

        CategoryEntity category2 = new CategoryEntity();
        category2.setRoom(room);
        category2.setCategoryName("음성 채널");
        category2 = categoryJpaRepository.save(category2);

        ChannelEntity channel1 = new ChannelEntity();
        channel1.setCategory(category1);
        channel1.setChannelName("일반");
        channel1.setChannelType(ChannelType.CHAT);
        channelJpaRepository.save(channel1);

        ChannelEntity channel2 = new ChannelEntity();
        channel2.setCategory(category2);
        channel2.setChannelName("일반");
        channel2.setChannelType(ChannelType.VOICE);
        channelJpaRepository.save(channel2);

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
        category1Channel.setChannelType(ChannelType.CHAT);

        // 음성 채널 카테고리 일반 채널
        RoomCreateWithCCDTO.Channel category2Channel = new RoomCreateWithCCDTO.Channel();
        category2Channel.setChannelId(channel2.getChannelId());
        category2Channel.setChannelName(channel2.getChannelName());
        category2Channel.setChannelType(ChannelType.VOICE);

        // 각 카테고리에 일반 채널 리스트로 할당
        categoryDTO1.setChannels(Arrays.asList(category1Channel));
        categoryDTO2.setChannels(Arrays.asList(category2Channel));

        // 방에 카테고리 리스트로 할당
        roomCreateWithCCDTO.setCategories(Arrays.asList(categoryDTO1, categoryDTO2));


        return roomCreateWithCCDTO;
    }

    @Override
    public void delete(Long roomId) throws ClassNotFoundException {
        RoomEntity room = roomJpaRepository.findByRoomId(roomId).orElseThrow(() -> new ClassNotFoundException("방이 존재하지 않습니다."));
        roomJpaRepository.delete(room);
    }

    private MemberEntity getMemberFromSession() {
        // 현재 인증된 사용자의 인증 객체를 가져옴
        String[] memberInfo = SecurityContextHolder.getContext().getAuthentication().getName().split(" ");
        String email = memberInfo[1];

        return memberJpaRepository.findByEmail(email).orElseThrow(() -> new NoMemberException("존재하지 않는 회원입니다."));
    }
}
