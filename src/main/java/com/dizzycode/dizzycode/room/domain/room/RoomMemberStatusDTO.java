package com.dizzycode.dizzycode.room.domain.room;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomMemberStatusDTO {

    private Long memberId;
    private String username;
    private String status;
}
