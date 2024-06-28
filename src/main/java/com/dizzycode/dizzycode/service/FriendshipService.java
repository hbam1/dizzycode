package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.Member;
import com.dizzycode.dizzycode.domain.friendship.Friendship;
import com.dizzycode.dizzycode.domain.friendship.FriendshipId;
import com.dizzycode.dizzycode.dto.friendship.FriendshipDetailDTO;
import com.dizzycode.dizzycode.dto.friendship.FriendshipRemoveDTO;
import com.dizzycode.dizzycode.repository.FriendshipRepository;
import com.dizzycode.dizzycode.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final MemberRepository memberRepository;

    public List<FriendshipDetailDTO> friendshipList(Long memberId) {

        List<Friendship> friendships = friendshipRepository.findFriendshipsByMemberId(memberId);

        List<FriendshipDetailDTO> friendshipDetailList = friendships.stream().filter(friendship -> friendship.getStatus() == Friendship.FriendshipStatus.ACCEPTED)
                .map(friendship -> {

                    FriendshipDetailDTO friendshipDetailDTO = new FriendshipDetailDTO();
                    Long friendId = findFriendId(memberId, friendship);
                    friendshipDetailDTO.setFriendId(friendId);
                    Optional<Member> optionalMember = memberRepository.findById(friendId);
                    optionalMember.ifPresent(member -> {
                        String friendName = member.getUsername();
                        friendshipDetailDTO.setFriendName(friendName);
                        friendshipDetailDTO.setCurrentStatus(Friendship.FriendshipStatus.ACCEPTED);
                    });
                    return friendshipDetailDTO;
                }
        ).toList();

        return friendshipDetailList;
    }

    public List<FriendshipDetailDTO> friendshipPendingList(Long memberId) {

        List<Friendship> friendships = friendshipRepository.findFriendshipsByMemberId(memberId);

        List<FriendshipDetailDTO> friendshipDetailList = friendships.stream().filter(friendship -> friendship.getStatus() == Friendship.FriendshipStatus.PENDING &&
                        friendship.getMember2().getId().equals(memberId))
                .map(friendship -> {

                    FriendshipDetailDTO friendshipDetailDTO = new FriendshipDetailDTO();
                    friendshipDetailDTO.setFriendId(friendship.getMember1().getId());
                    friendshipDetailDTO.setFriendName(friendship.getMember1().getUsername());
                    friendshipDetailDTO.setCurrentStatus(Friendship.FriendshipStatus.PENDING);
                    return friendshipDetailDTO;
                }
        ).toList();

        return friendshipDetailList;
    }

    public FriendshipDetailDTO requestFriendship(Long senderId, Long receiverId) {

        Friendship friendship = new Friendship();
        friendship.setId(new FriendshipId(senderId, receiverId));
        Optional<Member> optionalMember1 = memberRepository.findById(senderId);
        Optional<Member> optionalMember2 = memberRepository.findById(receiverId);
        if (optionalMember1.isPresent() && optionalMember2.isPresent()) {
            friendship.setMember1(optionalMember1.get());
            friendship.setMember2(optionalMember2.get());
        }

        friendshipRepository.save(friendship);

        FriendshipDetailDTO friendshipDetail = new FriendshipDetailDTO();
        friendshipDetail.setFriendId(friendship.getMember2().getId());
        friendshipDetail.setCurrentStatus(friendship.getStatus());
        friendshipDetail.setFriendName(friendship.getMember2().getUsername());

        return friendshipDetail;
    }

    public FriendshipDetailDTO requestFriendshipByUsername(Long senderId, String username) {

        Member friend = memberRepository.findByUsername(username);
        Long friendId = friend.getId();
        Friendship friendship = new Friendship();
        friendship.setId(new FriendshipId(senderId, friendId));
        Optional<Member> optionalMember1 = memberRepository.findById(senderId);
        if (optionalMember1.isPresent()) {
            friendship.setMember1(optionalMember1.get());
            friendship.setMember2(friend);
        }

        friendshipRepository.save(friendship);

        FriendshipDetailDTO friendshipDetail = new FriendshipDetailDTO();
        friendshipDetail.setFriendId(friendship.getMember2().getId());
        friendshipDetail.setCurrentStatus(friendship.getStatus());
        friendshipDetail.setFriendName(friendship.getMember2().getUsername());

        return friendshipDetail;
    }

    public FriendshipRemoveDTO removeFriendship(Long memberId1, Long memberId2) throws ClassNotFoundException {

        FriendshipId friendshipId = new FriendshipId(memberId1, memberId2);
        Friendship friendship = friendshipRepository.findFriendshipById(friendshipId);

        if (friendship == null) {
            throw new ClassNotFoundException("친구 관계가 존재하지 않습니다.");
        }

        friendshipRepository.delete(friendship);

        FriendshipRemoveDTO friendshipRemoveDTO = new FriendshipRemoveDTO();

        return friendshipRemoveDTO;
    }

    public FriendshipDetailDTO acceptFriendshipRequest(Long memberId1, Long memberId2) throws ClassNotFoundException {

        FriendshipId friendshipId = new FriendshipId(memberId1, memberId2);
        Friendship friendship = friendshipRepository.findFriendshipById(friendshipId);

        if (friendship == null) {
            throw new ClassNotFoundException("친구 관계가 존재하지 않습니다.");
        }

        friendship.setStatus(Friendship.FriendshipStatus.ACCEPTED);

        FriendshipDetailDTO friendshipDetail = new FriendshipDetailDTO();
        friendshipDetail.setFriendId(friendship.getMember2().getId());
        friendshipDetail.setCurrentStatus(friendship.getStatus());
        friendshipDetail.setFriendName(friendship.getMember2().getUsername());

        return friendshipDetail;
    }

    public FriendshipDetailDTO rejectFriendshipRequest(Long memberId1, Long memberId2) throws ClassNotFoundException {

        FriendshipId friendshipId = new FriendshipId(memberId1, memberId2);
        Friendship friendship = friendshipRepository.findFriendshipById(friendshipId);

        if (friendship == null) {
            throw new ClassNotFoundException("친구 관계가 존재하지 않습니다.");
        }

        friendship.setStatus(Friendship.FriendshipStatus.REJECTED);

        FriendshipDetailDTO friendshipDetail = new FriendshipDetailDTO();
        friendshipDetail.setFriendId(friendship.getMember2().getId());
        friendshipDetail.setCurrentStatus(friendship.getStatus());
        friendshipDetail.setFriendName(friendship.getMember2().getUsername());

        return friendshipDetail;
    }

    public Long findFriendId(Long memberId, Friendship friendship) {

        Long friendId;
        Long memberId1 = friendship.getMember1().getId();
        Long memberId2 = friendship.getMember2().getId();

        if (memberId.equals(memberId1)) {
            friendId = memberId2;
        } else {
            friendId = memberId1;
        }

        return friendId;
    }
}
