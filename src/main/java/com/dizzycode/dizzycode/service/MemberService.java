package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.Member;
import com.dizzycode.dizzycode.domain.enumerate.RoleEnum;
import com.dizzycode.dizzycode.dto.member.MemberSignupDTO;
import com.dizzycode.dizzycode.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;

    // 회원 이메일 혹은 닉네임 중복 확인
    public Boolean checkSameMember(MemberSignupDTO memberSignupDTO) {

        // 동일 회원 존재 여부 확인
        return memberRepository.existsByEmailOrUsername(memberSignupDTO.getEmail(), memberSignupDTO.getUsername());
    }

    // 회원 db에 저장
    public Member signUp(MemberSignupDTO memberSignupDTO) {

        Member member = new Member();

        member.setEmail(memberSignupDTO.getEmail());

        // 비밀번호 암호화
        member.setPassword(bCryptPasswordEncoder.encode(memberSignupDTO.getPassword()));
        member.setUsername(memberSignupDTO.getUsername());
        member.setRole(RoleEnum.ROLE_USER);

        return memberRepository.save(member);
    }
}
