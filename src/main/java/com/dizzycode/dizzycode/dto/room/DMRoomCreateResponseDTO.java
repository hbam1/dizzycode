package com.dizzycode.dizzycode.dto.room;

import com.dizzycode.dizzycode.domain.roommember.DMRoomMember;
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
