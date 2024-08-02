package com.dizzycode.dizzycode.friendship.infrastructure;

import com.dizzycode.dizzycode.friendship.domain.Friendship;
import com.dizzycode.dizzycode.friendship.domain.FriendshipId;
import com.dizzycode.dizzycode.friendship.service.port.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FriendshipRepositoryImpl implements FriendshipRepository {

    private final FriendshipJpaRepository friendshipJpaRepository;

    @Override
    public Optional<List<Friendship>> findFriendshipsByMemberId(Long memberId) {
        return Optional.of(friendshipJpaRepository.findFriendshipsByMemberId(memberId).get().stream()
                .map(friendshipEntity -> {
                    Friendship friendship = friendshipEntity.toModel();

                    return friendship;
                })
                .toList());
    }

    @Override
    public Optional<Friendship> findFriendshipById(Long memberId1, Long memberId2) {
        return friendshipJpaRepository.findFriendshipById(memberId1, memberId2).map(FriendshipEntity::toModel);
    }

    @Override
    public Friendship save(Friendship friendship) {
        return friendshipJpaRepository.save(FriendshipEntity.fromModel(friendship)).toModel();
    }

    @Override
    public void delete(Friendship friendship) {
        friendshipJpaRepository.delete(FriendshipEntity.fromModel(friendship));
    }

    @Override
    public Boolean existsById(FriendshipId friendshipId) {
        return friendshipJpaRepository.existsById(FriendshipIdEntity.fromModel(friendshipId));
    }
}
