package com.example.socketmessenger.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 채팅방 내 WebSocket 메시지를 처리하는 핸들러 (브로드캐스트, 필터링 등 포함)
 */
@Component
@RequiredArgsConstructor
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ChatRoomService chatRoomService;

    /**
     * 클라이언트로부터 수신한 채팅 메시지를 같은 채팅방에 연결된 모든 사용자에게 브로드캐스트하는 메서드
     *
     * 1. 현재 채널의 roomId 속성을 조회하여 사용자가 속한 채팅방을 확인
     * 2. roomId가 존재할 경우, 해당 채팅방에 연결된 모든 채널(Channel) 목록을 가져옴
     * 3. 채팅방에 속한 모든 사용자에게 메시지를 전송 (본인 포함)
     *
     * @param ctx Netty의 ChannelHandlerContext, 현재 채널 정보 포함
     * @param msg WebSocket 프레임으로 전달된 텍스트 메시지
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String message = msg.text();
        String roomId = (String) ctx.channel().attr(AttributeKey.valueOf("roomId")).get();

        if (roomId != null) {
            Set<Channel> receivers = chatRoomService.getChannelSet(roomId);
            for (Channel channel : receivers) {
                channel.writeAndFlush(new TextWebSocketFrame(message));
            }
        }
    }
}
