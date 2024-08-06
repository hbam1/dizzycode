package com.dizzycode.dizzycode.roommember.controller;

import com.dizzycode.dizzycode.roommember.domain.dto.RoomMemberDetailDTO;
import com.dizzycode.dizzycode.roommember.service.RoomMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomMemberController {

    private final RoomMemberService roomMemberService;

    @PostMapping("/rooms/{roomId}/in")
    public ResponseEntity<RoomMemberDetailDTO> roomIn(@PathVariable Long roomId) throws ClassNotFoundException {

        return new ResponseEntity<>(roomMemberService.roomIn(roomId), HttpStatus.OK);
    }
}
