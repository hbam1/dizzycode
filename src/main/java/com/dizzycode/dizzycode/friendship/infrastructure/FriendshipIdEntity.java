package com.dizzycode.dizzycode.friendship.infrastructure;

import com.dizzycode.dizzycode.friendship.domain.FriendshipId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Setter
public class FriendshipIdEntity implements Serializable {

    @Column(name = "member_id1")
    private Long memberId1;

    @Column(name = "member_id2")
    private Long memberId2;

    public FriendshipIdEntity(Long memberId1, Long memberId2) {
        this.memberId1 = memberId1;
        this.memberId2 = memberId2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipIdEntity that = (FriendshipIdEntity) o;
        return (Objects.equals(memberId1, that.memberId1) && Objects.equals(memberId2, that.memberId2)) ||
                (Objects.equals(memberId1, that.memberId2) && Objects.equals(memberId2, that.memberId1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId1) + Objects.hash(memberId2);
    }

    public static FriendshipIdEntity fromModel(FriendshipId friendshipId) {
        FriendshipIdEntity friendshipIdEntity = new FriendshipIdEntity();
        friendshipIdEntity.setMemberId1(friendshipId.getMemberId1());
        friendshipIdEntity.setMemberId2(friendshipId.getMemberId2());
        return friendshipIdEntity;
    }

    public FriendshipId toModel() {
        return new FriendshipId(memberId1, memberId2);
    }
}
