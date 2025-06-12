package com.example.socketmessenger.member.service;

import com.example.socketmessenger.auth.service.JwtService;
import com.example.socketmessenger.exception.NoSuchUserException;
import com.example.socketmessenger.exception.UnauthorizedException;
import com.example.socketmessenger.member.dto.request.MemberRegisterDto;
import com.example.socketmessenger.member.dto.response.MemberInfoDto;
import com.example.socketmessenger.member.entity.Member;
import com.example.socketmessenger.member.factory.MemberFactory;
import com.example.socketmessenger.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public void register(MemberRegisterDto memberRegisterDto) {
        Member member = MemberFactory.register(memberRegisterDto, passwordEncoder);
        memberRepository.save(member);
    }

    @Transactional
    public MemberInfoDto getMemberInfo(HttpServletRequest httpServletRequest) {
        String accessToken = jwtService.extractAccessToken(httpServletRequest)
                .orElseThrow(() -> new UnauthorizedException("Access Token이 없습니다."));

        String accountId = jwtService.extractAccountIdFromToken(accessToken);
        Optional<Member> memberOptional = memberRepository.findByAccountId(accountId);

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            return MemberInfoDto.builder()
                    .accountId(member.getAccountId())
                    .role(member.getRole().getKey())
                    .build();
        } else {
            throw new NoSuchUserException("사용자 정보가 존재하지 않습니다.");
        }
    }
}
