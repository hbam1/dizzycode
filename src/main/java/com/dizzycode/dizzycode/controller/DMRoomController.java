package com.dizzycode.dizzycode.controller;

import com.dizzycode.dizzycode.dto.room.DMRoomCreateDTO;
import com.dizzycode.dizzycode.dto.room.DMRoomCreateResponseDTO;
import com.dizzycode.dizzycode.dto.room.RoomDetailDTO;
import com.dizzycode.dizzycode.service.DirectMessageRoomService;
import com.dizzycode.dizzycode.service.DirectMessageService;
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
    public ResponseEntity<DMRoomCreateResponseDTO> createDMRoom(@RequestBody DMRoomCreateDTO dmRoomCreateDTO) {

        return new ResponseEntity<>(directMessageRoomService.createDMRoom(dmRoomCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/direct/rooms")
    public ResponseEntity<List<RoomDetailDTO>> roomList() {

        return new ResponseEntity<>(directMessageRoomService.roomList(), HttpStatus.OK);
    }

    @GetMapping("/direct/rooms/test/member/{memberId}")
    public ResponseEntity<List<RoomDetailDTO>> roomListForTest(@PathVariable Long memberId) {

        return new ResponseEntity<>(directMessageRoomService.roomListForTest(memberId), HttpStatus.OK);
    }
}
