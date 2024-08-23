package com.dizzycode.dizzycode.member.service;

import com.dizzycode.dizzycode.mock.member.FakeMemberRepository;
import com.dizzycode.dizzycode.mock.member.FakeMemberStatusRepository;
import com.dizzycode.dizzycode.mock.room.FakeRoomRepository;
import com.dizzycode.dizzycode.mock.roommember.FakeRoomMemberRepository;
import com.dizzycode.dizzycode.room.domain.room.RoomCreateDTO;
import com.dizzycode.dizzycode.room.domain.room.RoomCreateWithCCDTO;
import com.dizzycode.dizzycode.room.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class RoomServiceTest {

    private RoomService roomService;

    @BeforeEach
    void init() {
        this.roomService = RoomService.builder()
                .roomRepository(new FakeRoomRepository())
                .memberRepository(new FakeMemberRepository())
                .roomMemberRepository(new FakeRoomMemberRepository())
                .memberStatusRepository(new FakeMemberStatusRepository())
                .build();
    }

    @Test
    void 방_생성() {
        // given
        RoomCreateDTO roomCreateDTO = new RoomCreateDTO();
        roomCreateDTO.setRoomName("test");
        roomCreateDTO.setOpen(true);

        // when
        RoomCreateWithCCDTO roomCreateWithCCDTO = roomService.createRoom(roomCreateDTO);

        // then
        assertThat(roomCreateWithCCDTO.getRoomId()).isEqualTo(1L);
        assertThat(roomCreateWithCCDTO.getRoomName()).isEqualTo(roomCreateDTO.getRoomName());
        assertThat(roomCreateWithCCDTO.isOpen()).isEqualTo(roomCreateDTO.isOpen());
        assertThat(roomCreateWithCCDTO.getCategories().size()).isEqualTo(2);
        assertThat(roomCreateWithCCDTO.getCategories().get(0).getChannels().size()).isEqualTo(1);
        assertThat(roomCreateWithCCDTO.getCategories().get(1).getChannels().size()).isEqualTo(1);
    }

    @Test
    void 속해있는_방_목록() {
        // given
        RoomCreateDTO roomCreateDTO = new RoomCreateDTO();
        roomCreateDTO.setRoomName("test");
        roomCreateDTO.setOpen(true);
        RoomCreateWithCCDTO roomCreateWithCCDTO = roomService.createRoom(roomCreateDTO);

        // when
        roomService.list();
    }
}
