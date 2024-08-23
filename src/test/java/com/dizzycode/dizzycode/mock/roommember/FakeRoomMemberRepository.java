package com.dizzycode.dizzycode.mock.roommember;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.room.domain.Room;
import com.dizzycode.dizzycode.roommember.domain.RoomMember;
import com.dizzycode.dizzycode.roommember.domain.RoomMemberId;
import com.dizzycode.dizzycode.roommember.service.port.RoomMemberRepository;

import java.util.List;
import java.util.Optional;

public class FakeRoomMemberRepository implements RoomMemberRepository {

    @Override
    public List<Room> findRoomsByMemberId(Long memberID) {
        return null;
    }

    @Override
    public List<Member> findMembersByRoomId(Long roomId) {
        return null;
    }

    @Override
    public Optional<RoomMember> findRoomMemberByRoomMemberId(RoomMemberId roomMemberId) {
        return Optional.empty();
    }

    @Override
    public RoomMember save(Long roomId) throws ClassNotFoundException {
        return null;
    }

    @Override
    public void delete(RoomMember roomMember) {

    }
}
