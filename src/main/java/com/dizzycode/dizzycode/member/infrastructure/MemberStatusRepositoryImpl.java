package com.dizzycode.dizzycode.member.infrastructure;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.room.domain.room.RoomMemberStatusDTO;
import com.dizzycode.dizzycode.member.service.port.MemberStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberStatusRepositoryImpl implements MemberStatusRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void save(RoomMemberStatusDTO roomMemberStatusDTO, HashMap<String, String> userStatus) {
        redisTemplate.opsForHash().putAll("memberId:" + roomMemberStatusDTO.getMemberId(), userStatus);
    }

    public List<Object> membersStatus(List<Member> members) {
        // Redis pipeline을 사용하여 모든 멤버의 상태를 한 번에 가져옴
        return redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (Member member : members) {
                connection.hashCommands().hGet(("memberId:" + member.getId()).getBytes(), "status".getBytes());
            }

            return null;
        });
    }
}
