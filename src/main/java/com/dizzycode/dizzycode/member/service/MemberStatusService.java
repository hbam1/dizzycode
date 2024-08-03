package com.dizzycode.dizzycode.member.service;

import com.dizzycode.dizzycode.room.domain.room.RoomMemberStatusDTO;
import com.dizzycode.dizzycode.member.service.port.MemberStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class MemberStatusService {

    private final MemberStatusRepository memberStatusRepository;

    // 로그인 또는 로그아웃 시에 접속 상태 변경 API
    public void change(RoomMemberStatusDTO roomMemberStatusDTO) {
        HashMap<String, String> userStatus = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        userStatus.put("status", roomMemberStatusDTO.getStatus());
        userStatus.put("lastActive", now.toString());
        System.out.println(userStatus);

        memberStatusRepository.save(roomMemberStatusDTO, userStatus);
    }
}
