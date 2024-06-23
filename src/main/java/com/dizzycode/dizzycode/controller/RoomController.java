package com.dizzycode.dizzycode.controller;

import com.dizzycode.dizzycode.domain.Room;
import com.dizzycode.dizzycode.dto.room.RoomCreateDTO;
import com.dizzycode.dizzycode.dto.room.RoomDetailDTO;
import com.dizzycode.dizzycode.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/rooms")
    public ResponseEntity<Room> createRoom(@RequestBody RoomCreateDTO roomCreateDTO) {

        return new ResponseEntity<>(roomService.createRoom(roomCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> roomList() {

        return new ResponseEntity<>(roomService.roomList(), HttpStatus.OK);
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<RoomDetailDTO> roomRetrieve(@PathVariable Long roomId) {

        return new ResponseEntity<>(roomService.roomRetrieve(roomId), HttpStatus.OK);
    }
}
