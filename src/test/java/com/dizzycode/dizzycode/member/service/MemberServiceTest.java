package com.dizzycode.dizzycode.member.service;

import com.dizzycode.dizzycode.member.domain.Member;
import com.dizzycode.dizzycode.member.domain.MemberSignup;
import com.dizzycode.dizzycode.member.exception.ExistMemberException;
import com.dizzycode.dizzycode.member.service.port.MemberStatusRepository;
import com.dizzycode.dizzycode.mock.FakeMemberRepository;
import com.dizzycode.dizzycode.mock.FakeMemberStatusRepository;
import com.dizzycode.dizzycode.room.domain.room.RoomMemberStatusDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberStatusRepository memberStatusRepository = new FakeMemberStatusRepository();

    @BeforeEach
    void init() {
        this.memberService = MemberService.builder()
                .memberRepository(new FakeMemberRepository())
                .memberStatusRepository(memberStatusRepository)
                .bCryptPasswordEncoder(bCryptPasswordEncoder)
                .build();
    }

    @Test
    void 회원가입() {
        // given
        MemberSignup memberSignup = new MemberSignup();
        memberSignup.setEmail("test@test.com");
        memberSignup.setUsername("test");
        memberSignup.setPassword("password");

        // when
        when(bCryptPasswordEncoder.encode(memberSignup.getPassword())).thenReturn("encryptedPassword");
        Member member = memberService.signUp(memberSignup);

        //then
        assertThat(member.getId()).isEqualTo(1L);
        assertThat(member.getPassword()).isEqualTo("encryptedPassword");
    }

    @Test
    void 이메일이_같은_회원_가입_불갸() {
        // given
        MemberSignup memberGiven = new MemberSignup();
        memberGiven.setEmail("test@test.com");
        memberGiven.setUsername("test1");
        memberGiven.setPassword("password");
        memberService.signUp(memberGiven);

        // when
        MemberSignup memberSignup = new MemberSignup();
        memberSignup.setEmail("test@test.com");
        memberSignup.setUsername("test2");
        memberSignup.setPassword("password");

        // then
        assertThatThrownBy(() -> {
            memberService.signUp(memberGiven);
        }).isInstanceOf(ExistMemberException.class);
    }

    @Test
    void 닉네임이_같은_회원_가입_불갸() {
        // given
        MemberSignup memberGiven = new MemberSignup();
        memberGiven.setEmail("test1@test.com");
        memberGiven.setUsername("test");
        memberGiven.setPassword("password");
        memberService.signUp(memberGiven);

        // when
        MemberSignup memberSignup = new MemberSignup();
        memberSignup.setEmail("test2@test.com");
        memberSignup.setUsername("test");
        memberSignup.setPassword("password");

        // then
        assertThatThrownBy(() -> {
            memberService.signUp(memberGiven);
        }).isInstanceOf(ExistMemberException.class);
    }

    @Test
    void 회원_접속상태_변경() {
        // TODO
    }
}
