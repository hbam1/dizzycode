package com.dizzycode.dizzycode.roommember.infrastructure;

import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;
import com.dizzycode.dizzycode.room.infrastructure.RoomEntity;
import com.dizzycode.dizzycode.roommember.domain.RoomMember;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "room_members")
public class RoomMemberEntity {

    @EmbeddedId
    private RoomMemberIdEntity roomMemberId;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id", updatable = false)
    private MemberEntity member;

    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id", updatable = false)
    private RoomEntity room;

    @Column(nullable = false)
    private boolean manager;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static RoomMemberEntity fromModel(RoomMember roomMember) {
        RoomMemberEntity roomMemberEntity = new RoomMemberEntity();
        roomMemberEntity.setRoomMemberId(RoomMemberIdEntity.fromModel(roomMember.getRoomMemberId()));
        roomMemberEntity.setMember(MemberEntity.fromModel(roomMember.getMember()));
        roomMemberEntity.setRoom(RoomEntity.fromModel(roomMember.getRoom()));
        roomMemberEntity.setManager(roomMember.isManager());

        return roomMemberEntity;
    }

    public RoomMember toModel() {
        return RoomMember.builder()
                .roomMemberId(roomMemberId.toModel())
                .member(member.toModel())
                .room(room.toModel())
                .manager(manager)
                .createdAt(createdAt)
                .build();
    }
}
