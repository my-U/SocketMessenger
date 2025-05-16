package com.example.template.auth.service;

import com.example.template.auth.JwtService;
import com.example.template.auth.dto.response.TokenResponseDto;
import com.example.template.exception.UnauthorizedException;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.TOKEN_PREFIX}")
    private String TOKEN_PREFIX;

    @Value("${jwt.USE_BLACKLIST}")
    private boolean useBlacklist;

    /**
     * 로그아웃 처리: RefreshToken 제거 + AccessToken 블랙리스트 처리
     */
    @Transactional
    public void logout(HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new UnauthorizedException("Access Token이 없습니다."));

        if (!jwtService.validateToken(accessToken)) {
            throw new UnauthorizedException("유효하지 않은 Access Token입니다.");
        }

        String accountId = jwtService.extractAccountIdFromToken(accessToken);

        // 1. RefreshToken 제거
        redisTemplate.delete("REFRESH_TOKEN:" + accountId);

        // 2. AccessToken 블랙리스트 등록 (옵션)
        if (useBlacklist) {
            long expiration = jwtService.getRemainingExpiration(accessToken);
            redisTemplate.opsForValue().set(
                    "BLACKLIST:" + accessToken,
                    "logout",
                    Duration.ofMillis(expiration)
            );
        }
    }

    /**
     * 리프레시 토큰을 기반으로 새 Access Token 발급
     */
    @Transactional
    public TokenResponseDto refreshAccessToken(String refreshToken) {
        if (refreshToken == null || !jwtService.validateToken(refreshToken)) {
            throw new UnauthorizedException("유효하지 않은 Refresh Token입니다. 다시 로그인해주세요.");
        }

        String accountId = jwtService.extractAccountIdFromToken(refreshToken);

        // Redis에 저장된 RefreshToken과 비교하여 위조 여부 확인
        String redisRefreshToken = redisTemplate.opsForValue().get("REFRESH_TOKEN:" + accountId);
        if (redisRefreshToken == null || !redisRefreshToken.equals(refreshToken)) {
            throw new UnauthorizedException("저장된 Refresh Token과 일치하지 않습니다.");
        }

        // Access Token 재발급
        String newAccessToken = jwtService.createAccessToken(accountId);

        return new TokenResponseDto(newAccessToken, TOKEN_PREFIX);
    }
}
