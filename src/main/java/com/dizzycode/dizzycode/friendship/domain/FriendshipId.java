package com.dizzycode.dizzycode.friendship.domain;

import lombok.Getter;

@Getter
public class FriendshipId {

    private Long memberId1;
    private Long memberId2;

    public FriendshipId(Long memberId1, Long memberId2) {
        this.memberId1 = memberId1;
        this.memberId2 = memberId2;
    }
}
