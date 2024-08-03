package com.dizzycode.dizzycode.roommember.domain;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.room.domain.Room;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RoomMember {

    private RoomMemberId roomMemberId;
    private Member member;
    private Room room;
    private boolean manager;
    private LocalDateTime createdAt;

    @Builder
    public RoomMember(RoomMemberId roomMemberId, Member member, Room room, boolean manager, LocalDateTime createdAt) {
        this.roomMemberId = roomMemberId;
        this.member = member;
        this.room = room;
        this.manager = manager;
        this.createdAt = createdAt;
    }
}
