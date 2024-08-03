package com.dizzycode.dizzycode.roommember.infrastructure;

import com.dizzycode.dizzycode.roommember.domain.RoomMemberId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
public class RoomMemberIdEntity implements Serializable {

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "room_id")
    private Long roomId;

    public RoomMemberIdEntity(Long memberId, Long roomId) {
        this.memberId = memberId;
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomMemberIdEntity that = (RoomMemberIdEntity) o;
        return memberId == that.memberId && roomId == that.roomId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, roomId);
    }

    public static RoomMemberIdEntity fromModel(RoomMemberId roomMemberId) {
        return new RoomMemberIdEntity(roomMemberId.getMemberId(), roomMemberId.getRoomId());
    }

    public RoomMemberId toModel() {
        return new RoomMemberId(memberId, roomId);
    }
}
