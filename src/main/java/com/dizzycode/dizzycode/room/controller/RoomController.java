package com.dizzycode.dizzycode.room.controller;

import com.dizzycode.dizzycode.roommember.domain.dto.RoomMemberDetailDTO;
import com.dizzycode.dizzycode.member.domain.MemberStatus;
import com.dizzycode.dizzycode.room.domain.room.RoomCreateDTO;
import com.dizzycode.dizzycode.room.domain.room.RoomCreateWithCCDTO;
import com.dizzycode.dizzycode.room.domain.room.RoomDetailDTO;
import com.dizzycode.dizzycode.room.domain.room.RoomRemoveDTO;
import com.dizzycode.dizzycode.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/rooms")
    public ResponseEntity<RoomCreateWithCCDTO> createRoom(@RequestBody RoomCreateDTO roomCreateDTO) {

        return new ResponseEntity<>(roomService.createRoom(roomCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDetailDTO>> roomList() {

        return new ResponseEntity<>(roomService.list(), HttpStatus.OK);
    }

    @GetMapping("/rooms/all")
    public ResponseEntity<List<RoomDetailDTO>> findAll() {

        return new ResponseEntity<>(roomService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<RoomDetailDTO> roomRetrieve(@PathVariable Long roomId) throws ClassNotFoundException {

        return new ResponseEntity<>(roomService.roomRetrieve(roomId), HttpStatus.OK);
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<RoomRemoveDTO> roomRemove(@PathVariable Long roomId) throws ClassNotFoundException {

        return new ResponseEntity<>(roomService.roomRemove(roomId), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/rooms/{roomId}/in")
    public ResponseEntity<RoomMemberDetailDTO> roomIn(@PathVariable Long roomId) throws ClassNotFoundException {

        return new ResponseEntity<>(roomService.roomIn(roomId), HttpStatus.OK);
    }

    @DeleteMapping("/rooms/{roomId}/out")
    public ResponseEntity<Void> roomOut(@PathVariable Long roomId) {

        roomService.out(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/rooms/{roomId}/members")
    public ResponseEntity<List<MemberStatus>> roomMembers(@PathVariable Long roomId) {

        return new ResponseEntity<>(roomService.getRoomMembers(roomId), HttpStatus.OK);
    }
}
