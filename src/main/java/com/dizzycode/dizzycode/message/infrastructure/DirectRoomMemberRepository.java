package com.dizzycode.dizzycode.message.infrastructure;

import com.dizzycode.dizzycode.message.domain.DirectMessageRoom;
import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;
import com.dizzycode.dizzycode.message.domain.DMRoomMember;
import com.dizzycode.dizzycode.roommember.infrastructure.RoomMemberEntity;
import com.dizzycode.dizzycode.roommember.infrastructure.RoomMemberIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectRoomMemberRepository extends JpaRepository<DMRoomMember, RoomMemberIdEntity> {

    @Query("SELECT rm.room FROM DMRoomMember rm WHERE rm.roomMemberId.memberId = :memberId")
    List<DirectMessageRoom> findRoomsByMemberId(@Param("memberId") Long memberID);

    @Query("SELECT rm.member FROM DMRoomMember rm WHERE rm.roomMemberId.roomId = :roomId")
    List<MemberEntity> findMembersByRoomId(@Param("roomId") Long roomId);

    RoomMemberEntity findRoomMemberByRoomMemberId(RoomMemberIdEntity roomMemberIdEntity);
}
