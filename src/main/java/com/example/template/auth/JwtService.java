package com.example.template.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtService {
    @Value("${jwt.SECRET_KEY}")
    private String SECRET_KEY;

    @Value("${jwt.ACCESS.EXPIRATION_TIME}")
    private long ACCESS_EXPIRATION_TIME;

    @Value("${jwt.REFRESH.EXPIRATION_TIME}")
    private long REFRESH_EXPIRATION_TIME;

    @Value("${jwt.ACCESS.HEADER}")
    private String ACCESS_HEADER;

    @Value("${jwt.TOKEN_PREFIX}")
    private String TOKEN_PREFIX;
    private Algorithm algorithm;
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String ACCOUNT_CLAIM = "accountId";


    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(SECRET_KEY); // Auth0 방식에서는 Algorithm 사용
    }

    /**
     * AccessToken 생성
     */
    public String createAccessToken(String accountId) {
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT) // subject를 고정된 값으로 설정
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME)) // 만료 시간 설정
                .withClaim(ACCOUNT_CLAIM, accountId) // accountId를 Claim으로 저장
                .sign(algorithm); // 서명
    }

    /**
     * RefreshToken 생성
     */
    public String createRefreshToken(String accountId) {
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .withClaim(ACCOUNT_CLAIM, accountId)
                .sign(algorithm);
    }

    /**
     * JWT 토큰에서 사용자 계정 추출
     */
    public String extractAccountIdFromToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token).getClaim(ACCOUNT_CLAIM).asString(); // accountId 추출
    }

    /**
     * JWT 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            JWT.require(algorithm)
                    .build()
                    .verify(token);
            return true;
        } catch (TokenExpiredException e) {
            log.warn("Token 만료됨");
            return false;
        } catch (JWTVerificationException e) {
            log.warn("Token 유효하지 않음");
            return false;
        }
    }

    /**
     * AccessToken 추출
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader(ACCESS_HEADER);
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            return Optional.of(authHeader.substring(TOKEN_PREFIX.length()));
        }
        return Optional.empty();
    }

    /**
     * HttpOnly Cookie로 RefreshToken 저장
     */
    public void sendRefreshToken(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_SUBJECT, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) REFRESH_EXPIRATION_TIME); // 7일 유지
        response.addCookie(cookie);
    }

    public long getRemainingExpiration(String token) {
        Date expiresAt = JWT.require(algorithm)
                .build()
                .verify(token)
                .getExpiresAt();
        return expiresAt.getTime() - System.currentTimeMillis();
    }
}
