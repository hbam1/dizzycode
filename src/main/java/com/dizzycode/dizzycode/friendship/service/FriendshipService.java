package com.dizzycode.dizzycode.friendship.service;

import com.dizzycode.dizzycode.friendship.domain.Friendship;
import com.dizzycode.dizzycode.friendship.domain.FriendshipStatus;
import com.dizzycode.dizzycode.friendship.service.port.FriendshipRepository;
import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.friendship.domain.dto.FriendshipDetailDTO;
import com.dizzycode.dizzycode.friendship.domain.dto.FriendshipRemoveDTO;
import com.dizzycode.dizzycode.room.domain.room.DMRoomCreateDTO;
import com.dizzycode.dizzycode.member.service.port.MemberRepository;
import com.dizzycode.dizzycode.message.service.DirectMessageRoomService;
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
        List<Friendship> friendships = friendshipRepository.findFriendshipsByMemberId(memberId);

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
        List<Friendship> friendships = friendshipRepository.findFriendshipsByMemberId(memberId);

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
        Friendship friendship = friendshipRepository.saveById(senderId, receiverId);
        FriendshipDetailDTO friendshipDetail = new FriendshipDetailDTO();
        friendshipDetail.setFriendId(friendship.getMember2().getId());
        friendshipDetail.setCurrentStatus(friendship.getStatus());
        friendshipDetail.setFriendName(friendship.getMember2().getUsername());

        return friendshipDetail;
    }

    // 친구 요청(username 기반)
    @Transactional
    public FriendshipDetailDTO requestFriendshipByUsername(Long senderId, String username) {
        Friendship friendship = friendshipRepository.saveByUsername(senderId, username);
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
        Friendship friendship = friendshipRepository.accept(memberId1, memberId2);
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
        Friendship friendship = friendshipRepository.reject(memberId1, memberId2);
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
