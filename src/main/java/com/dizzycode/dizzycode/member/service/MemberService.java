package com.dizzycode.dizzycode.member.service;

import com.dizzycode.dizzycode.member.exception.ExistMemberException;
import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.member.domain.MemberSignup;
import com.dizzycode.dizzycode.member.service.port.MemberRepository;
import com.dizzycode.dizzycode.member.service.port.MemberStatusRepository;
import com.dizzycode.dizzycode.room.domain.room.RoomMemberStatusDTO;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Builder
public class MemberService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final MemberStatusRepository memberStatusRepository;

    // 회원가입
    @Transactional
    public Member signUp(MemberSignup memberSignup) {
        Boolean isExist = checkSameMember(memberSignup);
        if (isExist) {
            throw new ExistMemberException("이미 존재하는 이메일 또는 사용자 이름입니다.");
        }
        Member member = Member.from(memberSignup, bCryptPasswordEncoder.encode(memberSignup.getPassword()));

        return memberRepository.save(member);
    }

    // 로그인 또는 로그아웃 시에 접속 상태 변경 API
    public void change(RoomMemberStatusDTO roomMemberStatusDTO) {
        HashMap<String, String> userStatus = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        userStatus.put("status", roomMemberStatusDTO.getStatus());
        userStatus.put("lastActive", now.toString());

        memberStatusRepository.save(roomMemberStatusDTO, userStatus);
    }

    // 회원 이메일 혹은 닉네임 중복 확인
    public Boolean checkSameMember(MemberSignup memberSignup) {

        return memberRepository.existsByEmailOrUsername(memberSignup.getEmail(), memberSignup.getUsername());
    }
}
