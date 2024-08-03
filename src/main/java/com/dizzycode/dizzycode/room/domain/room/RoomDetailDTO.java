package com.dizzycode.dizzycode.room.domain.room;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDetailDTO {

    private Long roomId;
    private String roomName;
    private boolean open;
}
