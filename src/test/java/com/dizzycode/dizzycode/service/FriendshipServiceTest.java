package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.DirectMessageRoom;
import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;
import com.dizzycode.dizzycode.domain.friendship.Friendship;
import com.dizzycode.dizzycode.domain.friendship.FriendshipId;
import com.dizzycode.dizzycode.dto.friendship.FriendshipDetailDTO;
import com.dizzycode.dizzycode.dto.friendship.FriendshipRemoveDTO;
import com.dizzycode.dizzycode.repository.DirectMessageRoomRepository;
import com.dizzycode.dizzycode.repository.FriendshipRepository;
import com.dizzycode.dizzycode.member.infrastructure.MemberJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FriendshipServiceTest {

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private DirectMessageRoomRepository directMessageRoomRepository;

    @Mock
    private MemberJpaRepository memberJpaRepository;

    @InjectMocks
    private FriendshipService friendshipService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFriendshipList() {
        Long memberId = 1L;
        Friendship friendship = new Friendship();
        friendship.setMemberEntity1(new MemberEntity());
        friendship.setMemberEntity2(new MemberEntity());
        friendship.setStatus(Friendship.FriendshipStatus.ACCEPTED);
        List<Friendship> friendships = List.of(friendship);

        when(friendshipRepository.findFriendshipsByMemberId(memberId)).thenReturn(friendships);
        when(memberJpaRepository.findById(any(Long.class))).thenReturn(Optional.of(new MemberEntity()));

        List<FriendshipDetailDTO> result = friendshipService.friendshipList(memberId);

        assertEquals(1, result.size());
        assertEquals(Friendship.FriendshipStatus.ACCEPTED, result.get(0).getCurrentStatus());
    }

    @Test
    void testRequestFriendship() {
        Long senderId = 1L;
        Long receiverId = 2L;

        when(memberJpaRepository.findById(senderId)).thenReturn(Optional.of(new MemberEntity()));
        when(memberJpaRepository.findById(receiverId)).thenReturn(Optional.of(new MemberEntity()));
        when(friendshipRepository.existsById(any(FriendshipId.class))).thenReturn(false);

        FriendshipDetailDTO result = friendshipService.requestFriendship(senderId, receiverId);

        assertNotNull(result);
        assertEquals(receiverId, result.getFriendId());
    }

    @Test
    void testAcceptFriendshipRequest() throws ClassNotFoundException {
        Long memberId1 = 1L;
        Long memberId2 = 2L;
        Friendship friendship = new Friendship();
        friendship.setMemberEntity1(new MemberEntity());
        friendship.setMemberEntity2(new MemberEntity());
        friendship.setStatus(Friendship.FriendshipStatus.PENDING);

        when(friendshipRepository.findFriendshipById(memberId1, memberId2)).thenReturn(friendship);
        when(directMessageRoomRepository.save(any(DirectMessageRoom.class))).thenReturn(new DirectMessageRoom());
        when(memberJpaRepository.findById(memberId1)).thenReturn(Optional.of(new MemberEntity()));
        when(memberJpaRepository.findById(memberId2)).thenReturn(Optional.of(new MemberEntity()));

        FriendshipDetailDTO result = friendshipService.acceptFriendshipRequest(memberId1, memberId2);

        assertEquals(Friendship.FriendshipStatus.ACCEPTED, result.getCurrentStatus());
    }

    @Test
    void testRemoveFriendship() throws ClassNotFoundException {
        Long memberId1 = 1L;
        Long memberId2 = 2L;
        Friendship friendship = new Friendship();

        when(friendshipRepository.findFriendshipById(memberId1, memberId2)).thenReturn(friendship);

        FriendshipRemoveDTO result = friendshipService.removeFriendship(memberId1, memberId2);

        verify(friendshipRepository, times(1)).delete(friendship);
        assertNotNull(result);
    }
}
