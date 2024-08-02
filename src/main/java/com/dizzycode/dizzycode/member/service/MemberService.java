package com.dizzycode.dizzycode.member.service;

import com.dizzycode.dizzycode.member.domain.SecondaryToken;
import com.dizzycode.dizzycode.exception.member.ExistMemberException;
import com.dizzycode.dizzycode.exception.member.NoMemberException;
import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.member.domain.MemberSignup;
import com.dizzycode.dizzycode.member.service.port.MemberRepository;
import com.dizzycode.dizzycode.common.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    // 회원 이메일 혹은 닉네임 중복 확인
    public Boolean checkSameMember(MemberSignup memberSignup) {
        // 동일 회원 존재 여부 확인
        return memberRepository.existsByEmailOrUsername(memberSignup.getEmail(), memberSignup.getUsername());
    }

    // 회원가입
    public Member signUp(MemberSignup memberSignup) {
        // 중복 회원 확인
        Boolean isExist = checkSameMember(memberSignup);
        if (isExist) {
            throw new ExistMemberException("이미 존재하는 이메일 또는 사용자 이름입니다.");
        }

        Member member = Member.from(memberSignup, bCryptPasswordEncoder.encode(memberSignup.getPassword()));

        return memberRepository.save(member);
    }

    // web socket handshake 전에 사용하는 token 발급
    public SecondaryToken secondaryToken() {
        Member member = getMemberFromSession();
        String secondary = jwtUtil.createJwt("secondary", member.getEmail(), member.getRole().toString(), 30000L, member.getId(), member.getUsername());
        SecondaryToken secondaryToken = new SecondaryToken();
        secondaryToken.setSecondaryToken(secondary);

        return secondaryToken;
    }

    private Member getMemberFromSession() {
        // 현재 인증된 사용자의 인증 객체를 가져옴
        String[] memberInfo = SecurityContextHolder.getContext().getAuthentication().getName().split(" ");
        String email = memberInfo[1];
        return memberRepository.findByEmail(email).orElseThrow(() -> new NoMemberException("존재하지 않는 회원입니다."));
    }
}
