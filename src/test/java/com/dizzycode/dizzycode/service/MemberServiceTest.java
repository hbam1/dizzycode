package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.domain.Member;
import com.dizzycode.dizzycode.domain.enumerate.RoleEnum;
import com.dizzycode.dizzycode.dto.member.MemberSignupDTO;
import com.dizzycode.dizzycode.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private MemberService memberService; // Assuming the service class is MemberService

    private MemberSignupDTO memberSignupDTO;

    @BeforeEach
    public void setUp() {
        memberSignupDTO = new MemberSignupDTO();
        memberSignupDTO.setEmail("test@example.com");
        memberSignupDTO.setPassword("password123");
        memberSignupDTO.setUsername("testuser");
    }

    @Test
    public void testSignUp() {
        Member member = new Member();
        member.setEmail(memberSignupDTO.getEmail());
        member.setPassword("encryptedPassword");
        member.setUsername(memberSignupDTO.getUsername());
        member.setRole(RoleEnum.ROLE_USER);

        when(bCryptPasswordEncoder.encode(memberSignupDTO.getPassword())).thenReturn("encryptedPassword");
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        Member createdMember = memberService.signUp(memberSignupDTO);

        assertEquals(memberSignupDTO.getEmail(), createdMember.getEmail());
        assertEquals("encryptedPassword", createdMember.getPassword());
        assertEquals(memberSignupDTO.getUsername(), createdMember.getUsername());
        assertEquals(RoleEnum.ROLE_USER, createdMember.getRole());
    }
}
