package com.dizzycode.dizzycode.member.infrastructure;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.member.service.port.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;


    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(MemberEntity.fromModel(member)).toModel();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email).map(MemberEntity::toModel);
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        return memberJpaRepository.findByUsername(username).map(MemberEntity::toModel);
    }

    @Override
    public Boolean existsByEmailOrUsername(String email, String username) {
        return memberJpaRepository.existsByEmailOrUsername(email, username);
    }
}
