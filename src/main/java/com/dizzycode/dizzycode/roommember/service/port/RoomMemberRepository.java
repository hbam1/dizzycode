package com.dizzycode.dizzycode.roommember.service.port;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.room.domain.Room;
import com.dizzycode.dizzycode.roommember.domain.RoomMember;
import com.dizzycode.dizzycode.roommember.domain.RoomMemberId;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository {

    List<Room> findRoomsByMemberId();
    List<Member> findMembersByRoomId(Long roomId);
    Optional<RoomMember> findRoomMemberByRoomMemberId(Long roomId);
    RoomMember save(Long roomId) throws ClassNotFoundException;
    void delete(RoomMember roomMember);
}
