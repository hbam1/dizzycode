package com.dizzycode.dizzycode.service.jwt;


import com.dizzycode.dizzycode.domain.Member;
import com.dizzycode.dizzycode.dto.jwt.CustomUserDetails;
import com.dizzycode.dizzycode.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if (member == null) {

            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        return new CustomUserDetails(member);
    }
}
