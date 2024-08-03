package com.dizzycode.dizzycode.member.controller.response;

import com.dizzycode.dizzycode.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class MemberCreateResponse {

    private Long id;
    private String email;
    private String username;

    public static MemberCreateResponse from(Member member) {
        return MemberCreateResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .build();
    }
}
