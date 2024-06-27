package com.dizzycode.dizzycode.controller;

import com.dizzycode.dizzycode.dto.RoomMemberDetailDTO;
import com.dizzycode.dizzycode.dto.room.RoomCreateDTO;
import com.dizzycode.dizzycode.dto.room.RoomCreateWithCCDTO;
import com.dizzycode.dizzycode.dto.room.RoomDetailDTO;
import com.dizzycode.dizzycode.dto.room.RoomRemoveDTO;
import com.dizzycode.dizzycode.service.RoomService;
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

        return new ResponseEntity<>(roomService.roomList(), HttpStatus.OK);
    }

    @GetMapping("/rooms/all")
    public ResponseEntity<List<RoomDetailDTO>> roomAll() {

        return new ResponseEntity<>(roomService.roomAll(), HttpStatus.OK);
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
    public ResponseEntity<RoomMemberDetailDTO> roomIn(@PathVariable Long roomId) {

        return new ResponseEntity<>(roomService.roomIn(roomId), HttpStatus.OK);
    }

    @DeleteMapping("/rooms/{roomId}/out")
    public ResponseEntity<?> roomOut(@PathVariable Long roomId) {

        return new ResponseEntity<>(roomService.roomOut(roomId), HttpStatus.NO_CONTENT);
    }
}
