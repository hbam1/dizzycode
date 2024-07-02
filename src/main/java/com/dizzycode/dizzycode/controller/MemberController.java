package com.dizzycode.dizzycode.controller;

import com.dizzycode.dizzycode.domain.Member;
import com.dizzycode.dizzycode.dto.member.MemberDetailDTO;
import com.dizzycode.dizzycode.dto.member.MemberSignupDTO;
import com.dizzycode.dizzycode.exception.member.ExistMemberException;
import com.dizzycode.dizzycode.repository.MemberRepository;
import com.dizzycode.dizzycode.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @PostMapping("/signup")
    public ResponseEntity<MemberDetailDTO> signUp(@RequestBody MemberSignupDTO memberSignupDTO) {

        // 중복 회원 확인
        Boolean isExist = memberService.checkSameMember(memberSignupDTO);

        if (isExist) {
            throw new ExistMemberException("이미 존재하는 이메일 또는 사용자 이름입니다.");
        }

        // 회원 정보를 저장하고 결과를 반환
        Member member = memberService.signUp(memberSignupDTO);

        MemberDetailDTO memberDetailDTO = new MemberDetailDTO();
        memberDetailDTO.setId(member.getId());
        memberDetailDTO.setEmail(member.getEmail());
        memberDetailDTO.setUsername(member.getUsername());

        return new ResponseEntity<>(memberDetailDTO, HttpStatus.OK);
    }

    @GetMapping("/members/detail")
    public ResponseEntity<MemberDetailDTO> memberDetail() {
        Member member = getMemberFromSession();
        MemberDetailDTO memberDetailDTO = new MemberDetailDTO();
        memberDetailDTO.setId(member.getId());
        memberDetailDTO.setEmail(member.getEmail());
        memberDetailDTO.setUsername(member.getUsername());

        return new ResponseEntity<>(memberDetailDTO, HttpStatus.OK);
    }

    private Member getMemberFromSession() {
        // 현재 인증된 사용자의 인증 객체를 가져옴
        String[] memberInfo = SecurityContextHolder.getContext().getAuthentication().getName().split(" ");
        String email = memberInfo[1];

        return memberRepository.findByEmail(email);
    }
}
