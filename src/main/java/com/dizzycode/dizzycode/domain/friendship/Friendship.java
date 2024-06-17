package com.dizzycode.dizzycode.domain.friendship;

import com.dizzycode.dizzycode.domain.Member;
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
    @JoinColumn(name = "member_id1", insertable = false, updatable = false)
    private Member member1;

    @ManyToOne
    @MapsId("memberId2")
    @JoinColumn(name = "member_id2", insertable = false, updatable = false)
    private Member member2;

    public enum FriendshipStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }
}
