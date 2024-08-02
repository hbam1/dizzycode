package com.dizzycode.dizzycode.repository;

import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;
import com.dizzycode.dizzycode.domain.Room;
import com.dizzycode.dizzycode.domain.roommember.RoomMember;
import com.dizzycode.dizzycode.domain.roommember.RoomMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomMemberRepository extends JpaRepository<RoomMember, RoomMemberId> {

    @Query("SELECT rm.room FROM RoomMember rm WHERE rm.roomMemberId.memberId = :memberId")
    List<Room> findRoomsByMemberId(@Param("memberId") Long memberID);

    @Query("SELECT rm.member FROM RoomMember rm WHERE rm.roomMemberId.roomId = :roomId")
    List<MemberEntity> findMembersByRoomId(@Param("roomId") Long roomId);

    RoomMember findRoomMemberByRoomMemberId(RoomMemberId roomMemberId);
}
