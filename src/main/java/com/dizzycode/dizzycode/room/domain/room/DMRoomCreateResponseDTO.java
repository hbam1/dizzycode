package com.dizzycode.dizzycode.room.domain.room;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DMRoomCreateResponseDTO {

    private Long roomId;
    private String roomName;
}
