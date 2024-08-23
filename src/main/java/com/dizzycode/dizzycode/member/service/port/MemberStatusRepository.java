package com.dizzycode.dizzycode.member.service.port;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.room.domain.room.RoomMemberStatusDTO;

import java.util.HashMap;
import java.util.List;

public interface MemberStatusRepository {

    void save(RoomMemberStatusDTO roomMemberStatusDTO, HashMap<String, String> userStatus);
    List<Object> membersStatus(List<Member> members);
}
