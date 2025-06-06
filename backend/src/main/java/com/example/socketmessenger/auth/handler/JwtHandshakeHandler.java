package com.example.socketmessenger.auth.handler;

import com.example.socketmessenger.auth.service.JwtService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * WebSocket Handshake 단계에서 JWT 인증을 처리하는 핸들러
 * - HTTP 요청 URI의 쿼리 파라미터로 전달된 JWT 토큰을 검증
 * - 유효한 경우 사용자 식별자(accountId)를 Channel에 저장
 * - 인증 실패 시 소켓 연결을 즉시 종료
 */
@Component
@RequiredArgsConstructor
public class JwtHandshakeHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Value("${jwt.ACCESS.HEADER}")
    private String ACCESS_HEADER;

    @Value("${jwt.TOKEN_PREFIX}")
    private String TOKEN_PREFIX;

    private static final String ACCOUNT_CLAIM = "accountId";

    private final JwtService jwtService;

    /**
     * WebSocket 핸드셰이크 요청에서 JWT 토큰을 추출 및 검증하는 메서드
     * 쿼리 파라미터에서 token 값을 가져와 유효성 검증
     * 토큰이 유효하면 accountId를 추출하여 채널에 저장
     * 유효하지 않거나 토큰이 없을 경우 연결을 종료
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        // Authorization 헤더에서 토큰 추출
        String authHeader = req.headers().get(ACCESS_HEADER);
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            ctx.close(); // 토큰이 없거나 형식이 잘못된 경우 연결 종료
            return;
        }

        String token = authHeader.substring(TOKEN_PREFIX.length());

        if (!jwtService.validateToken(token)) {
            ctx.close(); // 토큰이 유효하지 않으면 연결 종료
            return;
        }

        // 토큰에서 사용자 ID 추출 후 채널에 저장
        String accountId = jwtService.extractAccountIdFromToken(token);
        ctx.channel().attr(AttributeKey.valueOf(ACCOUNT_CLAIM)).set(accountId);

        // 다음 핸들러로 메시지 전달
        ctx.fireChannelRead(req.retain());
    }
}
