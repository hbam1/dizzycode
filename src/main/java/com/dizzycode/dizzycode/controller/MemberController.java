package com.dizzycode.dizzycode.controller;

import com.dizzycode.dizzycode.domain.Member;
import com.dizzycode.dizzycode.dto.member.MemberDetailDTO;
import com.dizzycode.dizzycode.dto.member.MemberSignupDTO;
import com.dizzycode.dizzycode.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody MemberSignupDTO memberSignupDTO) {

        // 중복 회원 확인
        Boolean isExist = memberService.checkSameMember(memberSignupDTO);

        if (isExist) {
            return new ResponseEntity<>("이미 존재하는 이메일 또는 사용자 이름입니다.", HttpStatus.BAD_REQUEST);
        }

        // 회원 정보를 저장하고 결과를 반환
        Member member = memberService.signUp(memberSignupDTO);

        MemberDetailDTO memberDetailDTO = new MemberDetailDTO();
        memberDetailDTO.setId(member.getId());
        memberDetailDTO.setEmail(member.getEmail());
        memberDetailDTO.setUsername(member.getUsername());

        return new ResponseEntity<>(memberDetailDTO, HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {

        return "test";
    }
}
