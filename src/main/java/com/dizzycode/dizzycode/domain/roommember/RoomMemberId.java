package com.dizzycode.dizzycode.domain.roommember;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
public class RoomMemberId implements Serializable {

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "room_id")
    private Long roomId;

    public RoomMemberId(long memberId, long roomId) {
        this.memberId = memberId;
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomMemberId that = (RoomMemberId) o;
        return memberId == that.memberId && roomId == that.roomId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, roomId);
    }
}
