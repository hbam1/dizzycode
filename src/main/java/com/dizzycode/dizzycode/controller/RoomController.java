package com.dizzycode.dizzycode.controller;

import com.dizzycode.dizzycode.domain.Room;
import com.dizzycode.dizzycode.dto.room.RoomCreateDTO;
import com.dizzycode.dizzycode.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/test1234")
    public String test() {
        return "test";
    }

    @PostMapping("/rooms")
    public ResponseEntity<Room> createRoom(RoomCreateDTO roomCreateDTO) {

        return new ResponseEntity<>(roomService.createRoom(roomCreateDTO), HttpStatus.CREATED);
    }

//    @GetMapping
//    public ResponseEntity<List<Room>> roomList() {
//        // 현재 인증된 사용자의 인증 객체
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        String email = authentication.getName();
//        return new ResponseEntity<>(roomService.roomList(email), HttpStatus.OK);
//    }
}
