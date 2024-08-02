package com.dizzycode.dizzycode.friendship.service;

import com.dizzycode.dizzycode.exception.member.NoMemberException;
import com.dizzycode.dizzycode.friendship.domain.Friendship;
import com.dizzycode.dizzycode.friendship.domain.FriendshipId;
import com.dizzycode.dizzycode.friendship.domain.FriendshipStatus;
import com.dizzycode.dizzycode.friendship.service.port.FriendshipRepository;
import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.dto.friendship.FriendshipDetailDTO;
import com.dizzycode.dizzycode.dto.friendship.FriendshipRemoveDTO;
import com.dizzycode.dizzycode.dto.room.DMRoomCreateDTO;
import com.dizzycode.dizzycode.exception.friendship.FriendshipAlreadyExistsException;
import com.dizzycode.dizzycode.exception.friendship.InvalidFriendshipRequestException;
import com.dizzycode.dizzycode.member.service.port.MemberRepository;
import com.dizzycode.dizzycode.service.DirectMessageRoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final MemberRepository memberRepository;
    private final DirectMessageRoomService directMessageRoomService;

    // 친구 목록
    @Transactional
    public List<FriendshipDetailDTO> friendshipList(Long memberId) {

        List<Friendship> friendships = friendshipRepository.findFriendshipsByMemberId(memberId).orElse(new ArrayList<Friendship>());

        List<FriendshipDetailDTO> friendshipDetailList = friendships.stream().filter(friendship -> friendship.getStatus() == FriendshipStatus.ACCEPTED)
                .map(friendship -> {
                    FriendshipDetailDTO friendshipDetailDTO = new FriendshipDetailDTO();
                    Long friendId = findFriendId(memberId, friendship);
                    friendshipDetailDTO.setFriendId(friendId);
                    Optional<Member> member = memberRepository.findById(friendId);
                    member.ifPresent(mem -> {
                        String friendName = mem.getUsername();
                        friendshipDetailDTO.setFriendName(friendName);
                        friendshipDetailDTO.setCurrentStatus(FriendshipStatus.ACCEPTED);
                    });
                    return friendshipDetailDTO;
                }
        ).toList();

        return friendshipDetailList;
    }

    @Transactional
    public List<FriendshipDetailDTO> friendshipPendingList(Long memberId) {
        List<Friendship> friendships = friendshipRepository.findFriendshipsByMemberId(memberId).orElse(new ArrayList<Friendship>());

        List<FriendshipDetailDTO> friendshipDetailList = friendships.stream().filter(friendship -> friendship.getStatus() == FriendshipStatus.PENDING &&
                        friendship.getMember2().getId().equals(memberId))
                .map(friendship -> {
                    FriendshipDetailDTO friendshipDetailDTO = new FriendshipDetailDTO();
                    friendshipDetailDTO.setFriendId(friendship.getMember1().getId());
                    friendshipDetailDTO.setFriendName(friendship.getMember1().getUsername());
                    friendshipDetailDTO.setCurrentStatus(FriendshipStatus.PENDING);
                    return friendshipDetailDTO;
                }
        ).toList();

        return friendshipDetailList;
    }

    // 친구 신청 (id 기반)
    @Transactional
    public FriendshipDetailDTO requestFriendship(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) {
            throw new InvalidFriendshipRequestException("친구 신청을 보낼 수 없는 대상입니다.");
        }

        FriendshipId friendshipId1 = new FriendshipId(senderId, receiverId);
        FriendshipId friendshipId2 = new FriendshipId(receiverId, senderId);

        if (friendshipRepository.existsById(friendshipId1) || friendshipRepository.existsById(friendshipId2)) {
            throw new FriendshipAlreadyExistsException("친구 관계가 이미 존재합니다.");
        }

        Member optionalMember1 = memberRepository.findById(senderId).orElseThrow(() -> new NoMemberException("존재하지 않는 회원입니다."));
        Member optionalMember2 = memberRepository.findById(receiverId).orElseThrow(() -> new NoMemberException("존재하지 않는 회원입니다."));

        Friendship friendship = Friendship.builder()
                .friendshipId(friendshipId1)
                .status(FriendshipStatus.PENDING)
                .member1(optionalMember1)
                .member2(optionalMember2)
                .build();

        friendshipRepository.save(friendship);

        FriendshipDetailDTO friendshipDetail = new FriendshipDetailDTO();
        friendshipDetail.setFriendId(friendship.getMember2().getId());
        friendshipDetail.setCurrentStatus(friendship.getStatus());
        friendshipDetail.setFriendName(friendship.getMember2().getUsername());

        return friendshipDetail;
    }

    @Transactional
    public FriendshipDetailDTO requestFriendshipByUsername(Long senderId, String username) {
        Member friend = memberRepository.findByUsername(username).orElseThrow(() -> new NoMemberException("존재하지 않는 회원입니다."));
        Long friendId = friend.getId();
        if (senderId.equals(friendId)) {
            throw new InvalidFriendshipRequestException("친구 신청을 보낼 수 없는 대상입니다.");
        }

        FriendshipId friendshipId1 = new FriendshipId(senderId, friendId);
        FriendshipId friendshipId2 = new FriendshipId(friendId, senderId);
        if (friendshipRepository.existsById(friendshipId1) || friendshipRepository.existsById(friendshipId2)) {
            throw new FriendshipAlreadyExistsException("친구 관계가 이미 존재합니다.");
        }
        Member optionalMember1 = memberRepository.findById(senderId).orElseThrow(() -> new NoMemberException("존재하지 않는 회원입니다."));

        Friendship friendship = Friendship.builder()
                .friendshipId(friendshipId1)
                .status(FriendshipStatus.PENDING)
                .member1(optionalMember1)
                .member2(friend)
                .build();
        friendshipRepository.save(friendship);

        FriendshipDetailDTO friendshipDetail = new FriendshipDetailDTO();
        friendshipDetail.setFriendId(friendship.getMember2().getId());
        friendshipDetail.setCurrentStatus(friendship.getStatus());
        friendshipDetail.setFriendName(friendship.getMember2().getUsername());

        return friendshipDetail;
    }

    @Transactional
    // 친구 삭제
    public FriendshipRemoveDTO removeFriendship(Long memberId1, Long memberId2) throws ClassNotFoundException {
        Friendship friendship = friendshipRepository.findFriendshipById(memberId1, memberId2).orElseThrow(() -> new ClassNotFoundException("친구 관계가 존재하지 않습니다."));
        friendshipRepository.delete(friendship);
        FriendshipRemoveDTO friendshipRemoveDTO = new FriendshipRemoveDTO();

        return friendshipRemoveDTO;
    }

    // 친구 요청 수락
    @Transactional
    public FriendshipDetailDTO acceptFriendshipRequest(Long memberId1, Long memberId2) throws ClassNotFoundException {
        Friendship friendship = friendshipRepository.findFriendshipById(memberId1, memberId2).orElseThrow(() -> new ClassNotFoundException("친구 관계가 존재하지 않습니다."));
        friendship = friendship.update(FriendshipStatus.ACCEPTED);

        FriendshipDetailDTO friendshipDetail = new FriendshipDetailDTO();
        friendshipDetail.setFriendId(friendship.getMember2().getId());
        friendshipDetail.setCurrentStatus(friendship.getStatus());
        friendshipDetail.setFriendName(friendship.getMember2().getUsername());

        DMRoomCreateDTO dmRoomCreateDTO = new DMRoomCreateDTO();
        ArrayList<String> usernames = new ArrayList<>();
        usernames.add(friendship.getMember1().getUsername());
        usernames.add(friendship.getMember2().getUsername());
        dmRoomCreateDTO.setRoomName("");
        dmRoomCreateDTO.setUserNames(usernames);

        directMessageRoomService.createDMRoom(dmRoomCreateDTO);

        return friendshipDetail;
    }

    // 친구 요청 거절
    @Transactional
    public FriendshipDetailDTO rejectFriendshipRequest(Long memberId1, Long memberId2) throws ClassNotFoundException {
        Friendship friendship = friendshipRepository.findFriendshipById(memberId1, memberId2).orElseThrow(() -> new ClassNotFoundException("친구 관계가 존재하지 않습니다."));
        friendship = friendship.update(FriendshipStatus.REJECTED);

        FriendshipDetailDTO friendshipDetail = new FriendshipDetailDTO();
        friendshipDetail.setFriendId(friendship.getMember2().getId());
        friendshipDetail.setCurrentStatus(friendship.getStatus());
        friendshipDetail.setFriendName(friendship.getMember2().getUsername());

        return friendshipDetail;
    }

    @Transactional
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
