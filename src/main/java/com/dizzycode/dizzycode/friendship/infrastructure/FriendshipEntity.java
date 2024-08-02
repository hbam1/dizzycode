package com.dizzycode.dizzycode.friendship.infrastructure;

import com.dizzycode.dizzycode.friendship.domain.Friendship;
import com.dizzycode.dizzycode.friendship.domain.FriendshipStatus;
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
public class FriendshipEntity {

    @EmbeddedId
    private FriendshipIdEntity id;

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

    public static FriendshipEntity fromModel(Friendship friendship) {
        FriendshipEntity friendshipEntity = new FriendshipEntity();
        friendshipEntity.setId(FriendshipIdEntity.fromModel(friendship.getId()));
        friendshipEntity.setStatus(friendship.getStatus());
        friendshipEntity.setMemberEntity1(MemberEntity.fromModel(friendship.getMember1()));
        friendshipEntity.setMemberEntity2(MemberEntity.fromModel(friendship.getMember2()));

        return friendshipEntity;
    }

    public Friendship toModel() {
        return Friendship.builder()
                .friendshipId(id.toModel())
                .status(status)
                .member1(memberEntity1.toModel())
                .member2(memberEntity2.toModel())
                .build();
    }
}
