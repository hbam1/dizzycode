package com.dizzycode.dizzycode.friendship.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface FriendshipJpaRepository extends JpaRepository<FriendshipEntity, FriendshipIdEntity> {

    @Query("SELECT f FROM FriendshipEntity f WHERE f.id.memberId1 = :memberId OR f.id.memberId2 = :memberId")
    List<FriendshipEntity> findFriendshipsByMemberId(@Param("memberId") Long memberId);
    @Query("SELECT f FROM FriendshipEntity f WHERE (f.id.memberId1 = :memberId1 AND f.id.memberId2 = :memberId2) OR (f.id.memberId1 = :memberId2 AND f.id.memberId2 = :memberId1)")
    Optional<FriendshipEntity> findFriendshipById(@Param("memberId1") Long memberId1, @Param("memberId2") Long memberId2);
}
