package com.example.socketmessenger.chat;

import com.example.socketmessenger.auth.service.JwtService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;

import static com.example.socketmessenger.util.JsonUtil.extractTokenFromJson;

@RequiredArgsConstructor
public class AuthHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final JwtService jwtService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String text = msg.text();

        String token = extractTokenFromJson(text);
        if (!jwtService.validateToken(token)) {
            ctx.close(); // 인증 실패 시 연결 종료
            return;
        }

        ctx.channel().attr(AttributeKey.valueOf("userId")).set(jwtService.extractAccountIdFromToken(token));
        ctx.fireChannelRead(msg.retain()); // 다음 핸들러로 전달
    }
}

