package com.dizzycode.dizzycode.domain.friendship;

import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "friendships")
public class Friendship {

    @EmbeddedId
    private FriendshipId id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendshipStatus status = FriendshipStatus.PENDING;

    @ManyToOne
    @MapsId("memberId1")
    @JoinColumn(name = "member_id1", updatable = false)
    private MemberEntity memberEntity1;

    @ManyToOne
    @MapsId("memberId2")
    @JoinColumn(name = "member_id2", updatable = false)
    private MemberEntity memberEntity2;

    public enum FriendshipStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }
}
