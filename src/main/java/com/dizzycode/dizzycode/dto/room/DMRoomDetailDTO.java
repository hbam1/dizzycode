package com.dizzycode.dizzycode.dto.room;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DMRoomDetailDTO extends RoomDetailDTO {

    int memberCount;
    String temporaryRoomName;
}
