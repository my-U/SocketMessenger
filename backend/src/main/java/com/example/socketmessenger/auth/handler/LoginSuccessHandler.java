package com.example.socketmessenger.auth.handler;

import com.example.socketmessenger.auth.service.JwtService;
import com.example.socketmessenger.member.entity.Member;
import com.example.socketmessenger.auth.dto.response.LoginSuccessMemberDto;
import com.example.socketmessenger.auth.dto.response.LoginSuccessDto;
import com.example.socketmessenger.auth.dto.response.TokenResponseDto;
import com.example.socketmessenger.repository.MemberRepository;
import com.example.socketmessenger.util.enums.SuccessCode;
import com.example.socketmessenger.util.enums.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.token-prefix}")
    private String TOKEN_PREFIX;

    @Value("${media-type.json}")
    private String mediaTypeJson;

    @Value("${jwt.refresh.expiration-time}")
    private long REFRESH_EXPIRATION_TIME;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String accountId = authentication.getName();

        // JWT 생성
        String accessToken = jwtService.createAccessToken(accountId);

        TokenResponseDto tokenResponseDto = new TokenResponseDto(accessToken, TOKEN_PREFIX);

        Member member = memberRepository.findByAccountId(accountId)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));

        LoginSuccessMemberDto loginSuccessMemberDto = LoginSuccessMemberDto.builder()
                .accountId(member.getAccountId())
                .role(member.getRole())
                .build();

        String refreshToken = jwtService.createRefreshToken(accountId);

        redisTemplate.opsForValue().set("REFRESH_TOKEN:" + accountId, refreshToken, Duration.ofMillis(REFRESH_EXPIRATION_TIME));

        jwtService.sendRefreshToken(response, refreshToken);
        response.setContentType(mediaTypeJson);

        // JSON 응답 반환
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        new SuccessResponse(SuccessCode.LOGIN_SUCCESS, new LoginSuccessDto(tokenResponseDto, loginSuccessMemberDto))
                )
        );
    }
}
