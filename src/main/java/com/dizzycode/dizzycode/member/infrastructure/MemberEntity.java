package com.dizzycode.dizzycode.member.infrastructure;

import com.dizzycode.dizzycode.member.domain.Role;
import com.dizzycode.dizzycode.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "members")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // 암호화하여 저장

    @Column(nullable = false, unique = true)
    private String username; // 닉네임

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public static MemberEntity fromModel(Member member) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setEmail(member.getEmail());
        memberEntity.setPassword(member.getPassword());
        memberEntity.setUsername(member.getUsername());
        memberEntity.setRole(member.getRole());

        return memberEntity;
    }

    public Member toModel() {
        return Member.builder()
                .id(id)
                .email(email)
                .password(password)
                .username(username)
                .role(role)
                .build();
    }
}
