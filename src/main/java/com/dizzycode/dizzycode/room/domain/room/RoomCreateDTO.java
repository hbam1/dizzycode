package com.dizzycode.dizzycode.room.domain.room;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RoomCreateDTO {

    private String roomName;
    private boolean open;
}
