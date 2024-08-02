package com.dizzycode.dizzycode.member.controller;

import com.dizzycode.dizzycode.member.controller.response.MemberCreateResponse;
import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.member.domain.SecondaryToken;
import com.dizzycode.dizzycode.member.domain.MemberSignup;
import com.dizzycode.dizzycode.dto.room.RoomMemberStatusDTO;
import com.dizzycode.dizzycode.member.service.MemberService;
import com.dizzycode.dizzycode.member.service.MemberStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberStatusService memberStatusService;


    @PostMapping("/signup")
    public ResponseEntity<MemberCreateResponse> signUp(@RequestBody MemberSignup memberSignup) {

        // 회원 정보를 저장하고 결과를 반환
        Member member = memberService.signUp(memberSignup);

        return new ResponseEntity<>(MemberCreateResponse.from(member), HttpStatus.OK);
    }

    @GetMapping("/secondary-token")
    public ResponseEntity<SecondaryToken> getSecondaryToken() {

        return new ResponseEntity<>(memberService.secondaryToken(), HttpStatus.OK);
    }

    // 접속 상태 변경 API
    @PostMapping("/members/status")
    public ResponseEntity<Void> memberStatusChange(@RequestBody RoomMemberStatusDTO roomMemberStatusDTO) {
        memberStatusService.change(roomMemberStatusDTO);

        return new ResponseEntity(HttpStatus.OK);
    }
}
