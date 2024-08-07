package com.dizzycode.dizzycode.room.domain.room;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class DMRoomDetailDTO extends RoomDetailDTO {

    int memberCount;
    List<String> userNames;
    String temporaryRoomName;
    boolean close;
    boolean groupChat;
}
