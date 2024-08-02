package com.dizzycode.dizzycode.member.service.port;

import com.dizzycode.dizzycode.dto.room.RoomMemberStatusDTO;

import java.util.HashMap;

public interface MemberStatusRepository {

    void save(RoomMemberStatusDTO roomMemberStatusDTO, HashMap<String, String> userStatus);
}
