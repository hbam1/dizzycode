package com.dizzycode.dizzycode.controller;

import com.dizzycode.dizzycode.member.domain.MemberStatus;
import com.dizzycode.dizzycode.dto.room.DMRoomCreateDTO;
import com.dizzycode.dizzycode.dto.room.DMRoomDetailDTO;
import com.dizzycode.dizzycode.service.DirectMessageRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DMRoomController {

    private final DirectMessageRoomService directMessageRoomService;

    @PostMapping("/direct/rooms")
    public ResponseEntity<DMRoomDetailDTO> createDMRoom(@RequestBody DMRoomCreateDTO dmRoomCreateDTO) {
        return new ResponseEntity<>(directMessageRoomService.createDMRoom(dmRoomCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/direct/rooms")
    public ResponseEntity<List<DMRoomDetailDTO>> roomList() {
        return new ResponseEntity<>(directMessageRoomService.roomList(), HttpStatus.OK);
    }

    @GetMapping("/direct/rooms/{roomId}")
    public ResponseEntity<DMRoomDetailDTO> retrieveDMRoom(@PathVariable Long roomId) {
        return new ResponseEntity<>(directMessageRoomService.retrieveDMRoom(roomId), HttpStatus.OK);
    }

    @DeleteMapping("/direct/rooms/{roomId}")
    public ResponseEntity<String> deleteDMRoom(@PathVariable Long roomId) {
        directMessageRoomService.deleteDMRoom(roomId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/direct/rooms/{roomId}/members/{username}")
    public ResponseEntity<DMRoomDetailDTO> addMemberToDMRoom(@PathVariable Long roomId, @PathVariable String username) {
        return new ResponseEntity<>(directMessageRoomService.addMemberToDMRoom(roomId, username), HttpStatus.OK);
    }

    @DeleteMapping("/direct/rooms/{roomId}/members/{username}")
    public ResponseEntity<String> removeMemberFromDMRoom(@PathVariable Long roomId, @PathVariable String username) {
        directMessageRoomService.removeMemberFromDMRoom(roomId, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/direct/rooms/{roomId}/members")
    public ResponseEntity<List<MemberStatus>> roomMembers(@PathVariable Long roomId) {

        return new ResponseEntity<>(directMessageRoomService.getRoomMembers(roomId), HttpStatus.OK);
    }
}
