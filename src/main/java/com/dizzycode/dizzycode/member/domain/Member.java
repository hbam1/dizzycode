package com.dizzycode.dizzycode.member.domain;

import com.dizzycode.dizzycode.domain.enumerate.RoleEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private final String username;
    private final RoleEnum role;

    @Builder
    public Member(Long id, String email, String password, String username, RoleEnum role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
    }

    public static Member from(MemberSignup memberSignup, String encodedPassword) {
        return Member.builder()
                .email(memberSignup.getEmail())
                .password(encodedPassword)
                .username(memberSignup.getUsername())
                .role(RoleEnum.ROLE_USER)
                .build();
    }
}
