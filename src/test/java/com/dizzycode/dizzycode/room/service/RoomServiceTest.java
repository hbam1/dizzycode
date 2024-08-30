package com.dizzycode.dizzycode.room.service;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.member.domain.Role;
import com.dizzycode.dizzycode.mock.member.FakeMemberRepository;
import com.dizzycode.dizzycode.mock.member.FakeMemberStatusRepository;
import com.dizzycode.dizzycode.mock.room.FakeRoomIndexer;
import com.dizzycode.dizzycode.mock.room.FakeRoomRepository;
import com.dizzycode.dizzycode.mock.roommember.FakeRoomMemberRepository;
import com.dizzycode.dizzycode.room.domain.room.RoomCreateDTO;
import com.dizzycode.dizzycode.room.domain.room.RoomCreateWithCCDTO;
import com.dizzycode.dizzycode.room.domain.room.RoomDetailDTO;
import com.dizzycode.dizzycode.room.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class RoomServiceTest {

    private RoomService roomService;

    @BeforeEach
    void init() {
        FakeMemberRepository memberRepository = new FakeMemberRepository();
        FakeRoomMemberRepository roomMemberRepository = new FakeRoomMemberRepository(memberRepository);
        // member 미리 생성
        memberRepository.save(Member.builder()
                .email("test@test.com")
                .username("test")
                .password("password")
                .role(Role.ROLE_USER)
                .build());

        this.roomService = RoomService.builder()
                .roomIndexer(new FakeRoomIndexer())
                .roomRepository(new FakeRoomRepository(roomMemberRepository))
                .memberRepository(memberRepository)
                .roomMemberRepository(roomMemberRepository)
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
        roomService.createRoom(roomCreateDTO);

        // when
        List<RoomDetailDTO> list = roomService.list();

        // then
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0).getRoomId()).isEqualTo(1L);
    }
}
