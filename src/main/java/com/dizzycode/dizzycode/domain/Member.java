package com.dizzycode.dizzycode.domain;

import com.dizzycode.dizzycode.domain.enumerate.RoleEnum;
import com.dizzycode.dizzycode.domain.friendship.Friendship;
import com.dizzycode.dizzycode.domain.roommember.RoomMember;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter @Setter
@Table(name = "members")
public class Member {

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
    private RoleEnum role;
}
