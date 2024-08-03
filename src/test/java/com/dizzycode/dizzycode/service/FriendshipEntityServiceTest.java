package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.message.domain.DirectMessageRoom;
import com.dizzycode.dizzycode.friendship.domain.FriendshipStatus;
import com.dizzycode.dizzycode.friendship.service.FriendshipService;
import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;
import com.dizzycode.dizzycode.friendship.infrastructure.FriendshipEntity;
import com.dizzycode.dizzycode.friendship.infrastructure.FriendshipIdEntity;
import com.dizzycode.dizzycode.friendship.domain.dto.FriendshipDetailDTO;
import com.dizzycode.dizzycode.friendship.domain.dto.FriendshipRemoveDTO;
import com.dizzycode.dizzycode.message.infrastructure.DirectMessageRoomRepository;
import com.dizzycode.dizzycode.friendship.infrastructure.FriendshipJpaRepository;
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

public class FriendshipEntityServiceTest {

    @Mock
    private FriendshipJpaRepository friendshipJpaRepository;

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
        FriendshipEntity friendship = new FriendshipEntity();
        friendship.setMemberEntity1(new MemberEntity());
        friendship.setMemberEntity2(new MemberEntity());
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        List<FriendshipEntity> friendships = List.of(friendship);

        when(friendshipJpaRepository.findFriendshipsByMemberId(memberId)).thenReturn(friendships);
        when(memberJpaRepository.findById(any(Long.class))).thenReturn(Optional.of(new MemberEntity()));

        List<FriendshipDetailDTO> result = friendshipService.friendshipList(memberId);

        assertEquals(1, result.size());
        assertEquals(FriendshipStatus.ACCEPTED, result.get(0).getCurrentStatus());
    }

    @Test
    void testRequestFriendship() {
        Long senderId = 1L;
        Long receiverId = 2L;

        when(memberJpaRepository.findById(senderId)).thenReturn(Optional.of(new MemberEntity()));
        when(memberJpaRepository.findById(receiverId)).thenReturn(Optional.of(new MemberEntity()));
        when(friendshipJpaRepository.existsById(any(FriendshipIdEntity.class))).thenReturn(false);

        FriendshipDetailDTO result = friendshipService.requestFriendship(senderId, receiverId);

        assertNotNull(result);
        assertEquals(receiverId, result.getFriendId());
    }

    @Test
    void testAcceptFriendshipRequest() throws ClassNotFoundException {
        Long memberId1 = 1L;
        Long memberId2 = 2L;
        FriendshipEntity friendship = new FriendshipEntity();
        friendship.setMemberEntity1(new MemberEntity());
        friendship.setMemberEntity2(new MemberEntity());
        friendship.setStatus(FriendshipStatus.PENDING);

        when(friendshipJpaRepository.findFriendshipById(memberId1, memberId2)).thenReturn(friendship);
        when(directMessageRoomRepository.save(any(DirectMessageRoom.class))).thenReturn(new DirectMessageRoom());
        when(memberJpaRepository.findById(memberId1)).thenReturn(Optional.of(new MemberEntity()));
        when(memberJpaRepository.findById(memberId2)).thenReturn(Optional.of(new MemberEntity()));

        FriendshipDetailDTO result = friendshipService.acceptFriendshipRequest(memberId1, memberId2);

        assertEquals(FriendshipStatus.ACCEPTED, result.getCurrentStatus());
    }

    @Test
    void testRemoveFriendship() throws ClassNotFoundException {
        Long memberId1 = 1L;
        Long memberId2 = 2L;
        FriendshipEntity friendship = new FriendshipEntity();

        when(friendshipJpaRepository.findFriendshipById(memberId1, memberId2)).thenReturn(friendship);

        FriendshipRemoveDTO result = friendshipService.removeFriendship(memberId1, memberId2);

        verify(friendshipJpaRepository, times(1)).delete(friendship);
        assertNotNull(result);
    }
}
