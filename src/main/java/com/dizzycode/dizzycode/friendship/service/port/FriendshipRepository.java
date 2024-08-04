package com.dizzycode.dizzycode.friendship.service.port;

import com.dizzycode.dizzycode.friendship.domain.Friendship;
import com.dizzycode.dizzycode.friendship.domain.FriendshipId;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository {

    List<Friendship> findFriendshipsByMemberId(Long memberId);
    Optional<Friendship> findFriendshipById(Long memberId1, Long memberId2);
    Friendship saveByUsername(Long senderId, String username);
    Friendship saveById(Long senderId, Long receiverId);
    Friendship accept(Long memberId1, Long memberId2) throws ClassNotFoundException;
    Friendship reject(Long memberId1, Long memberId2) throws ClassNotFoundException;
    void delete(Friendship friendship);
    Boolean existsById(FriendshipId friendshipId);
}
