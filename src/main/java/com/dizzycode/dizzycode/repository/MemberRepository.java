package com.dizzycode.dizzycode.repository;

import com.dizzycode.dizzycode.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);
    Member findByUsername(String username);
    boolean existsByEmailOrUsername(String email, String username);
}
