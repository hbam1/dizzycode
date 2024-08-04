package com.dizzycode.dizzycode.friendship.infrastructure;

import com.dizzycode.dizzycode.friendship.domain.Friendship;
import com.dizzycode.dizzycode.friendship.domain.FriendshipId;
import com.dizzycode.dizzycode.friendship.domain.FriendshipStatus;
import com.dizzycode.dizzycode.friendship.exception.FriendshipAlreadyExistsException;
import com.dizzycode.dizzycode.friendship.exception.InvalidFriendshipRequestException;
import com.dizzycode.dizzycode.friendship.service.port.FriendshipRepository;
import com.dizzycode.dizzycode.member.exception.NoMemberException;
import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;
import com.dizzycode.dizzycode.member.infrastructure.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FriendshipRepositoryImpl implements FriendshipRepository {

    private final FriendshipJpaRepository friendshipJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public List<Friendship> findFriendshipsByMemberId(Long memberId) {
        return friendshipJpaRepository.findFriendshipsByMemberId(memberId).stream()
                .map(friendshipEntity -> {
                    Friendship friendship = friendshipEntity.toModel();

                    return friendship;
                })
                .toList();
    }

    @Override
    public Optional<Friendship> findFriendshipById(Long memberId1, Long memberId2) {
        return friendshipJpaRepository.findFriendshipById(memberId1, memberId2).map(FriendshipEntity::toModel);
    }

    @Override
    public Friendship saveByUsername(Long senderId, String username) {
        MemberEntity friend = memberJpaRepository.findByUsername(username).orElseThrow(() -> new NoMemberException("존재하지 않는 회원입니다."));
        Long friendId = friend.getId();
        if (senderId.equals(friendId)) {
            throw new InvalidFriendshipRequestException("친구 신청을 보낼 수 없는 대상입니다.");
        }

        FriendshipIdEntity friendshipId1 = new FriendshipIdEntity(senderId, friendId);
        FriendshipIdEntity friendshipId2 = new FriendshipIdEntity(friendId, senderId);
        if (friendshipJpaRepository.existsById(friendshipId1) || friendshipJpaRepository.existsById(friendshipId2)) {
            throw new FriendshipAlreadyExistsException("친구 관계가 이미 존재합니다.");
        }
        MemberEntity optionalMember1 = memberJpaRepository.findById(senderId).orElseThrow(() -> new NoMemberException("존재하지 않는 회원입니다."));

        FriendshipEntity friendship = new FriendshipEntity();
        friendship.setId(friendshipId1);
        friendship.setStatus(FriendshipStatus.PENDING);
        friendship.setMemberEntity1(optionalMember1);
        friendship.setMemberEntity2(friend);

        return friendshipJpaRepository.save(friendship).toModel();
    }

    @Override
    public Friendship saveById(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) {
            throw new InvalidFriendshipRequestException("친구 신청을 보낼 수 없는 대상입니다.");
        }

        FriendshipIdEntity friendshipId1 = new FriendshipIdEntity(senderId, receiverId);
        FriendshipIdEntity friendshipId2 = new FriendshipIdEntity(receiverId, senderId);

        if (friendshipJpaRepository.existsById(friendshipId1) || friendshipJpaRepository.existsById(friendshipId2)) {
            throw new FriendshipAlreadyExistsException("친구 관계가 이미 존재합니다.");
        }

        MemberEntity optionalMember1 = memberJpaRepository.findById(senderId).orElseThrow(() -> new NoMemberException("존재하지 않는 회원입니다."));
        MemberEntity optionalMember2 = memberJpaRepository.findById(receiverId).orElseThrow(() -> new NoMemberException("존재하지 않는 회원입니다."));

        FriendshipEntity friendship = new FriendshipEntity();
        friendship.setId(friendshipId1);
        friendship.setStatus(FriendshipStatus.PENDING);
        friendship.setMemberEntity1(optionalMember1);
        friendship.setMemberEntity2(optionalMember2);

        return friendshipJpaRepository.save(friendship).toModel();
    }

    @Override
    public Friendship accept(Long memberId1, Long memberId2) throws ClassNotFoundException {
        FriendshipEntity friendship = friendshipJpaRepository.findFriendshipById(memberId1, memberId2).orElseThrow(() -> new ClassNotFoundException("친구 관계가 존재하지 않습니다."));
        friendship.setStatus(FriendshipStatus.ACCEPTED);

        return friendshipJpaRepository.save(friendship).toModel();
    }

    @Override
    public Friendship reject(Long memberId1, Long memberId2) throws ClassNotFoundException {
        FriendshipEntity friendship = friendshipJpaRepository.findFriendshipById(memberId1, memberId2).orElseThrow(() -> new ClassNotFoundException("친구 관계가 존재하지 않습니다."));
        friendship.setStatus(FriendshipStatus.REJECTED);

        return friendshipJpaRepository.save(friendship).toModel();
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
