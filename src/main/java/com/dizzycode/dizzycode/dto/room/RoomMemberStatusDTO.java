package com.dizzycode.dizzycode.dto.room;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomMemberStatusDTO {

    private Long memberId;
    private String username;
    private String status;
}
