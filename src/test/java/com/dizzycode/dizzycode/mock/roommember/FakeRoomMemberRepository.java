package com.dizzycode.dizzycode.mock.roommember;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.mock.member.FakeMemberRepository;
import com.dizzycode.dizzycode.room.domain.Room;
import com.dizzycode.dizzycode.roommember.domain.RoomMember;
import com.dizzycode.dizzycode.roommember.domain.RoomMemberId;
import com.dizzycode.dizzycode.roommember.service.port.RoomMemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeRoomMemberRepository implements RoomMemberRepository {

    private FakeMemberRepository memberRepository;
    public List<RoomMember> data = new ArrayList<>();

    public FakeRoomMemberRepository(FakeMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public List<Room> findRoomsByMemberId() {
        return data.stream()
                .filter(roomMember -> roomMember.getRoomMemberId().getMemberId().equals(1L))
                .map(RoomMember::getRoom)
                .collect(Collectors.toList());
    }

    @Override
    public List<Member> findMembersByRoomId(Long roomId) {
        return null;
    }

    @Override
    public Optional<RoomMember> findRoomMemberByRoomMemberId(Long roomId) {
        return Optional.empty();
    }

    @Override
    public RoomMember save(Long roomId) {
        Optional<Member> member = memberRepository.findByEmail("test@test.com");
        RoomMemberId roomMemberId = new RoomMemberId(member.get().getId(), roomId);
        Room room = Room.builder()
                .roomId(roomId)
                .roomName("test")
                .open(true)
                .build();
        RoomMember roomMember = RoomMember.builder()
                .roomMemberId(roomMemberId)
                .room(room)
                .build();
        data.add(roomMember);
        return roomMember;
    }

    @Override
    public void delete(RoomMember roomMember) {

    }
}
