package com.dizzycode.dizzycode.member.infrastructure;

import com.dizzycode.dizzycode.dto.room.RoomMemberStatusDTO;
import com.dizzycode.dizzycode.member.service.port.MemberStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class MemberStatusRepositoryImpl implements MemberStatusRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void save(RoomMemberStatusDTO roomMemberStatusDTO, HashMap<String, String> userStatus) {
        redisTemplate.opsForHash().putAll("memberId:" + roomMemberStatusDTO.getMemberId(), userStatus);
    }
}
