package com.dizzycode.dizzycode.room.domain.room;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class DMRoomCreateDTO {

    private String roomName;
    private List<String> userNames;
}
