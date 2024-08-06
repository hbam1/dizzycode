package com.dizzycode.dizzycode.member.controller;

import com.dizzycode.dizzycode.member.controller.response.MemberCreateResponse;
import com.dizzycode.dizzycode.member.domain.MemberSignup;
import com.dizzycode.dizzycode.room.domain.room.RoomMemberStatusDTO;
import com.dizzycode.dizzycode.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<MemberCreateResponse> signUp(@RequestBody MemberSignup memberSignup) {

        return new ResponseEntity<>(MemberCreateResponse.from(memberService.signUp(memberSignup)), HttpStatus.OK);
    }

    // 접속 상태 변경 API
    @PostMapping("/members/status")
    public ResponseEntity<Void> memberStatusChange(@RequestBody RoomMemberStatusDTO roomMemberStatusDTO) {
        memberService.change(roomMemberStatusDTO);

        return new ResponseEntity(HttpStatus.OK);
    }
}
