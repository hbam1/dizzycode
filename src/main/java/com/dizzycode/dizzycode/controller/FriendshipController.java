package com.dizzycode.dizzycode.controller;

import com.dizzycode.dizzycode.dto.friendship.FriendshipDetailDTO;
import com.dizzycode.dizzycode.dto.friendship.FriendshipRemoveDTO;
import com.dizzycode.dizzycode.service.FriendshipService;
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

    @GetMapping("/friendship/member/{memberId}")
    public ResponseEntity<List<FriendshipDetailDTO>> friendList(@PathVariable Long memberId) {

        return new ResponseEntity<>(friendshipService.friendshipList(memberId), HttpStatus.OK);
    }

    @GetMapping("/friendship/pending/member/{memberId}")
    public ResponseEntity<List<FriendshipDetailDTO>> pendingList(@PathVariable Long memberId) {

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
