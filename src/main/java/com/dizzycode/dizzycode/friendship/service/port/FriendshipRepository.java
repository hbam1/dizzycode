package com.dizzycode.dizzycode.friendship.service.port;

import com.dizzycode.dizzycode.friendship.domain.Friendship;
import com.dizzycode.dizzycode.friendship.domain.FriendshipId;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository {

    Optional<List<Friendship>> findFriendshipsByMemberId(Long memberId);
    Optional<Friendship> findFriendshipById(Long memberId1, Long memberId2);
    Friendship save(Friendship friendship);
    void delete(Friendship friendship);
    Boolean existsById(FriendshipId friendshipId);
}
