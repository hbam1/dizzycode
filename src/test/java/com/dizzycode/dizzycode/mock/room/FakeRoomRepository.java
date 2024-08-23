package com.dizzycode.dizzycode.mock.room;

import com.dizzycode.dizzycode.category.domain.Category;
import com.dizzycode.dizzycode.channel.domain.Channel;
import com.dizzycode.dizzycode.channel.domain.ChannelType;
import com.dizzycode.dizzycode.mock.roommember.FakeRoomMemberRepository;
import com.dizzycode.dizzycode.room.domain.Room;
import com.dizzycode.dizzycode.room.domain.room.RoomCreateDTO;
import com.dizzycode.dizzycode.room.domain.room.RoomCreateWithCCDTO;
import com.dizzycode.dizzycode.room.service.port.RoomRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FakeRoomRepository implements RoomRepository {

    private Long autoGeneratedId = 0L;
    public List<Room> data = new ArrayList<>();
    private FakeRoomMemberRepository roomMemberRepository;

    public FakeRoomRepository(FakeRoomMemberRepository roomMemberRepository) {
        this.roomMemberRepository = roomMemberRepository;
    }

    @Override
    public Optional<Room> findByRoomId(Long roomId) {
        return Optional.empty();
    }

    @Override
    public List<Room> findRoomsByOpenIs(boolean open) {
        return null;
    }

    @Override
    public RoomCreateWithCCDTO save(RoomCreateDTO roomCreateDTO) {
        // 방 생성
        Room room = Room.builder()
                .roomId(++autoGeneratedId)
                .roomName(roomCreateDTO.getRoomName())
                .open(roomCreateDTO.isOpen())
                .build();
        // 방과 방 주인 설정
        roomMemberRepository.save(room.getRoomId());
        // 카테고리 및 채널 기본 생성 (채팅 카테고리와 음성 카테고리 기본 생성 및 각 카테고리 별 일반 채널 기본 생성)
        Category category1 = Category.builder()
                .categoryId(1L)
                .room(room)
                .categoryName("채팅 채널")
                .build();
        Category category2 = Category.builder()
                .categoryId(2L)
                .room(room)
                .categoryName("음성 채널")
                .build();
        Channel channel1 = Channel.builder()
                .channelId(1L)
                .category(category1)
                .channelName("일반")
                .channelType(ChannelType.CHAT)
                .build();
        Channel channel2 = Channel.builder()
                .channelId(1L)
                .category(category2)
                .channelName("일반")
                .channelType(ChannelType.VOICE)
                .build();
        data.add(room);
        // 반환 DTO
        // 방 객체
        RoomCreateWithCCDTO roomCreateWithCCDTO = new RoomCreateWithCCDTO();
        roomCreateWithCCDTO.setRoomId(room.getRoomId());
        roomCreateWithCCDTO.setRoomName(room.getRoomName());
        roomCreateWithCCDTO.setOpen(room.isOpen());

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
        category1Channel.setChannelType(channel1.getChannelType());

        // 음성 채널 카테고리 일반 채널
        RoomCreateWithCCDTO.Channel category2Channel = new RoomCreateWithCCDTO.Channel();
        category2Channel.setChannelId(channel2.getChannelId());
        category2Channel.setChannelName(channel2.getChannelName());
        category2Channel.setChannelType(channel2.getChannelType());

        // 각 카테고리에 일반 채널 리스트로 할당
        categoryDTO1.setChannels(Arrays.asList(category1Channel));
        categoryDTO2.setChannels(Arrays.asList(category2Channel));

        // 방에 카테고리 리스트로 할당
        roomCreateWithCCDTO.setCategories(Arrays.asList(categoryDTO1, categoryDTO2));

        return roomCreateWithCCDTO;
    }

    @Override
    public void delete(Long roomId) {

    }
}
