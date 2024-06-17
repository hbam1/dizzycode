package com.dizzycode.dizzycode.domain.friendship;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
public class FriendshipId implements Serializable {
    @Column(name = "member_id1")
    private Long memberId1;

    @Column(name = "member_id2")
    private Long memberId2;

    public FriendshipId(Long memberId1, Long memberId2) {
        if (memberId1 < memberId2) {
            this.memberId1 = memberId1;
            this.memberId2 = memberId2;
        } else {
            this.memberId1 = memberId2;
            this.memberId2 = memberId1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipId that = (FriendshipId) o;
        return Objects.equals(memberId1, that.memberId1) &&
                Objects.equals(memberId2, that.memberId2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId1, memberId2);
    }
}
