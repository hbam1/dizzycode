package com.dizzycode.dizzycode.friendship.controller;

import com.dizzycode.dizzycode.friendship.domain.dto.FriendshipDetailDTO;
import com.dizzycode.dizzycode.friendship.domain.dto.FriendshipRemoveDTO;
import com.dizzycode.dizzycode.friendship.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/friendship/member1/{senderId}/member2/{receiverId}")
    public ResponseEntity<FriendshipDetailDTO> requestFriendship(@PathVariable Long senderId, @PathVariable Long receiverId) {

        return new ResponseEntity<>(friendshipService.requestFriendship(senderId, receiverId), HttpStatus.CREATED);
    }

    @PostMapping("/friendship/member1/{senderId}/member2_name/{username}")
    public ResponseEntity<FriendshipDetailDTO> requestFriendshipByUsername(@PathVariable Long senderId, @PathVariable String username) {

        return new ResponseEntity<>(friendshipService.requestFriendshipByUsername(senderId, username), HttpStatus.CREATED);
    }

    @GetMapping("/friendship/member/{memberId}")
    public ResponseEntity<List<FriendshipDetailDTO>> friendList(@PathVariable(name = "memberId") Long memberId) {

        return new ResponseEntity<>(friendshipService.friendshipList(memberId), HttpStatus.OK);
    }

    @GetMapping("/friendship/pending/member/{memberId}")
    public ResponseEntity<List<FriendshipDetailDTO>> pendingList(@PathVariable(name = "memberId") Long memberId) {

        return new ResponseEntity<>(friendshipService.friendshipPendingList(memberId), HttpStatus.OK);
    }

    @PostMapping("/friendship/reject/member1/{memberId1}/member2/{memberId2}")
    public ResponseEntity<FriendshipDetailDTO> rejectFriendshipRequest(@PathVariable Long memberId1, @PathVariable Long memberId2) throws ClassNotFoundException {

        return new ResponseEntity<>(friendshipService.rejectFriendshipRequest(memberId1, memberId2), HttpStatus.OK);
    }

    @PostMapping("/friendship/accept/member1/{memberId1}/member2/{memberId2}")
    public ResponseEntity<FriendshipDetailDTO> acceptFriendshipRequest(@PathVariable Long memberId1, @PathVariable Long memberId2) throws ClassNotFoundException {

        return new ResponseEntity<>(friendshipService.acceptFriendshipRequest(memberId1, memberId2), HttpStatus.OK);
    }

    @DeleteMapping("/friendship/member1/{memberId1}/member2/{memberId2}")
    public ResponseEntity<FriendshipRemoveDTO> removeFriendship(@PathVariable Long memberId1, @PathVariable Long memberId2) throws ClassNotFoundException {

        return new ResponseEntity<>(friendshipService.removeFriendship(memberId1, memberId2), HttpStatus.NO_CONTENT);
    }
}