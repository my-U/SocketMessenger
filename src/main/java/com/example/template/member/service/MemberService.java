package com.example.template.member.service;

import com.example.template.member.dto.request.MemberRegisterDto;
import com.example.template.member.entity.Member;
import com.example.template.member.factory.MemberFactory;
import com.example.template.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(MemberRegisterDto memberRegisterDto) {
        Member member = MemberFactory.register(memberRegisterDto, passwordEncoder);
        memberRepository.save(member);
    }

}
