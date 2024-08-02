package com.dizzycode.dizzycode.friendship.domain;

import com.dizzycode.dizzycode.friendship.service.port.FriendshipRepository;
import com.dizzycode.dizzycode.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class Friendship {

    private final FriendshipId id;
    private final FriendshipStatus status;
    private final Member member1;
    private final Member member2;

    @Builder
    public Friendship(FriendshipId friendshipId, FriendshipStatus status, Member member1, Member member2) {
        this.id = friendshipId;
        this.status = status;
        this.member1 = member1;
        this.member2 = member2;
    }

    public Friendship update(FriendshipStatus friendshipStatus) {
        return Friendship.builder()
                .friendshipId(id)
                .status(friendshipStatus)
                .member1(member1)
                .member2(member2)
                .build();
    }
}
