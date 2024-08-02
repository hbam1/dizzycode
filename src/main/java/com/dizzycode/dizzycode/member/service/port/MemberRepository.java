package com.dizzycode.dizzycode.member.service.port;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUsername(String username);
    Boolean existsByEmailOrUsername(String email, String username);
}
