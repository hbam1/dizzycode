package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.member.infrastructure.MemberEntity;
import com.dizzycode.dizzycode.domain.enumerate.RoleEnum;
import com.dizzycode.dizzycode.member.domain.MemberSignup;
import com.dizzycode.dizzycode.member.infrastructure.MemberJpaRepository;
import com.dizzycode.dizzycode.member.service.MemberService;
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
public class MemberEntityServiceTest {

    @Mock
    private MemberJpaRepository memberJpaRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private MemberService memberService; // Assuming the service class is MemberService

    private MemberSignup memberSignup;

    @BeforeEach
    public void setUp() {
        memberSignup = new MemberSignup();
        memberSignup.setEmail("test@example.com");
        memberSignup.setPassword("password123");
        memberSignup.setUsername("testuser");
    }

    @Test
    public void testSignUp() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setEmail(memberSignup.getEmail());
        memberEntity.setPassword("encryptedPassword");
        memberEntity.setUsername(memberSignup.getUsername());
        memberEntity.setRole(RoleEnum.ROLE_USER);

        when(bCryptPasswordEncoder.encode(memberSignup.getPassword())).thenReturn("encryptedPassword");
        when(memberJpaRepository.save(any(MemberEntity.class))).thenReturn(memberEntity);

        Member createdMember = memberService.signUp(memberSignup);

        assertEquals(memberSignup.getEmail(), createdMember.getEmail());
        assertEquals("encryptedPassword", createdMember.getPassword());
        assertEquals(memberSignup.getUsername(), createdMember.getUsername());
        assertEquals(RoleEnum.ROLE_USER, createdMember.getRole());
    }
}
