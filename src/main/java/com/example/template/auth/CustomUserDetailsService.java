package com.example.template.auth;

import com.example.template.member.entity.Member;
import com.example.template.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        Member member = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        return new com.example.NPKI.security.CustomUserDetails(member, Collections.emptyList());
    }
}
