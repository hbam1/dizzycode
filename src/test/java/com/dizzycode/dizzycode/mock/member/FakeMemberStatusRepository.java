package com.dizzycode.dizzycode.mock.member;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.member.service.port.MemberStatusRepository;
import com.dizzycode.dizzycode.room.domain.room.RoomMemberStatusDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeMemberStatusRepository implements MemberStatusRepository {

    private Map<String, HashMap<String, String>> data = new HashMap<>();

    @Override
    public void save(RoomMemberStatusDTO roomMemberStatusDTO, HashMap<String, String> userStatus) {
        String key = "memberId:" + roomMemberStatusDTO.getMemberId();
        // 새로 생성한 HashMap을 기존 데이터와 병합
        data.put(key, userStatus);
    }

    @Override
    public List<Object> membersStatus(List<Member> members) {
        return null;
    }
}
