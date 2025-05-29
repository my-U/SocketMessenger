package com.example.socketmessenger.member.service;

import com.example.socketmessenger.member.dto.request.MemberRegisterDto;
import com.example.socketmessenger.member.entity.Member;
import com.example.socketmessenger.member.factory.MemberFactory;
import com.example.socketmessenger.repository.MemberRepository;
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
