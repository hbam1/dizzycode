package com.dizzycode.dizzycode.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByEmail(String email);
    Optional<MemberEntity> findByUsername(String username);
    Boolean existsByEmailOrUsername(String email, String username);
}
