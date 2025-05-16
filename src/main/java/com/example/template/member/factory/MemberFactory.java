package com.example.template.member.factory;

import com.example.template.member.dto.request.MemberRegisterDto;
import com.example.template.member.entity.Member;
import com.example.template.util.enums.Role;

public class MemberFactory {

    public static Member register(MemberRegisterDto memberRegisterDto, PasswordEncoder encoder) {
        return Member.builder()
                .accountId(memberRegisterDto.getAccountId())
                .password(encoder.encode(memberRegisterDto.getPassword()))
                .role(Role.USER)
                .build();
    }
}
