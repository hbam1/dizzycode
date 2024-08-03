package com.dizzycode.dizzycode.roommember.infrastructure;

import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;
import com.dizzycode.dizzycode.room.infrastructure.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomMemberJpaRepository extends JpaRepository<RoomMemberEntity, RoomMemberIdEntity> {

    @Query("SELECT rm.room FROM RoomMemberEntity rm WHERE rm.roomMemberId.memberId = :memberId")
    List<RoomEntity> findRoomsByMemberId(@Param("memberId") Long memberID);

    @Query("SELECT rm.member FROM RoomMemberEntity rm WHERE rm.roomMemberId.roomId = :roomId")
    List<MemberEntity> findMembersByRoomId(@Param("roomId") Long roomId);

    Optional<RoomMemberEntity> findRoomMemberByRoomMemberId(RoomMemberIdEntity roomMemberIdEntity);
}
