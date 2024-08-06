package com.dizzycode.dizzycode.security.service;

import com.dizzycode.dizzycode.common.JWTUtil;
import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.member.domain.SecondaryToken;
import com.dizzycode.dizzycode.member.exception.NoMemberException;
import com.dizzycode.dizzycode.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    // web socket handshake 전에 사용하는 token 발급
    @Transactional
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
