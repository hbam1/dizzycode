package com.dizzycode.dizzycode.roommember.domain;

import lombok.Getter;

@Getter
public class RoomMemberId {

    private Long memberId;
    private Long roomId;

    public RoomMemberId(Long memberId, Long roomId) {
        this.memberId = memberId;
        this.roomId = roomId;
    }


}
