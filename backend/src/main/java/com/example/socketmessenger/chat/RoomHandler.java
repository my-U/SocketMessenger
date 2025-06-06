package com.example.socketmessenger.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 채팅방 입장, 퇴장 및 관련 이벤트를 처리하는 WebSocket 핸들러
 */
@Component
@RequiredArgsConstructor
public class RoomHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ChatRoomService chatRoomService;

    /**
     * 사용자의 채팅방 입장을 처리하는 메서드
     * "/join {roomId} {accountId}" 명령을 해석하고 유효성을 검증
     * 채팅방 존재 여부를 확인 후 사용자(Channel)를 등록
     * 형식 오류 또는 존재하지 않는 방에 대한 요청은 무시
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();

        if (text.startsWith("/join ")) {
            String[] parts = text.substring(6).trim().split(" ", 2);
            if (parts.length < 2) {
                ctx.writeAndFlush(new TextWebSocketFrame("잘못된 입장 명령입니다."));
                return;
            }

            String roomId = parts[0];
            String accountId = parts[1];

            // 존재하지 않는 채팅방에 대한 요청은 무시
            if (!chatRoomService.exists(roomId)) {
                ctx.writeAndFlush(new TextWebSocketFrame("존재하지 않는 채팅방입니다."));
                return;
            }

            ctx.channel().attr(AttributeKey.valueOf("roomId")).set(roomId);
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(accountId);

            chatRoomService.addUserChannel(roomId, accountId, ctx.channel());
        } else {
            ctx.fireChannelRead(msg.retain());
        }
    }

    /**
     * 사용자의 연결이 종료될 때 연결된 채널의 roomId, accountId를 추출해 해당 사용자를 채팅방에서 제거
     * 채팅방 내 사용자가 모두 나가면 해당 방 정보 제거
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String roomId = (String) ctx.channel().attr(AttributeKey.valueOf("roomId")).get();
        String accountId = (String) ctx.channel().attr(AttributeKey.valueOf("accountId")).get();

        if (roomId != null && accountId != null) {
            chatRoomService.removeUserChannel(roomId, accountId, ctx.channel());
        }
    }
}