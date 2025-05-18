package com.example.socketmessenger.member.factory;

import com.example.socketmessenger.member.dto.request.MemberRegisterDto;
import com.example.socketmessenger.member.entity.Member;
import com.example.socketmessenger.util.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberFactory {

    public static Member register(MemberRegisterDto memberRegisterDto, PasswordEncoder encoder) {
        return Member.builder()
                .accountId(memberRegisterDto.getAccountId())
                .password(encoder.encode(memberRegisterDto.getPassword()))
                .role(Role.USER)
                .build();
    }
}
