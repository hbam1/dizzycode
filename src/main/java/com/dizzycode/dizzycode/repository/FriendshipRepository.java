package com.dizzycode.dizzycode.repository;

import com.dizzycode.dizzycode.domain.friendship.Friendship;
import com.dizzycode.dizzycode.domain.friendship.FriendshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {

    @Query("SELECT f FROM Friendship f WHERE f.id.memberId1 = :memberId OR f.id.memberId2 = :memberId")
    List<Friendship> findFriendshipsByMemberId(@Param("memberId") Long memberId);
    Friendship findFriendshipById(FriendshipId friendshipId);
}
