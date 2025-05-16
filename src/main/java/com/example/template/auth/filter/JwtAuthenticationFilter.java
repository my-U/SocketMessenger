package com.example.template.auth.filter;

import com.example.NPKI.exception.UnauthorizedException;
import com.example.NPKI.security.CustomUserDetailsService;
import com.example.NPKI.security.JwtService;
import com.example.NPKI.util.enums.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터
 * 모든 요청에서 JWT 검증
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String NO_CHECK_URLS = "/member/login";
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Spring Security의 OncePerRequestFilter를 확장할 때 구현해야 하는 필터 로직
     * 모든 HTTP 요청에 대해 한 번만 실행되는 필터이며, 요청을 가로채서 특정 작업을 수행할 수 있음
     * 현재 코드에서는 JWT 검증 및 인증(SecurityContext 설정) 작업 수행
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        /**
         * 로그인 요청은 검증 제외
         * 다음 필터 호출
         */
        if (NO_CHECK_URLS.contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }
        try{

            String token = jwtService.extractAccessToken(request).orElse(null);

            if (token == null || !jwtService.validateToken(token)) {
                throw new UnauthorizedException("토큰이 없거나 토큰 검증 실패") {};
            }
            String accountId = jwtService.extractAccountIdFromAccessToken(token);

            if (accountId == null) {
                throw new AuthenticationException("JWT에서 사용자 ID 추출 실패") {};
            }

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(accountId);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (UnauthorizedException e) {

            ErrorResponse.sendErrorResponse(response, e);
        }
    }
}
