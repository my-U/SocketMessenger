package com.example.socketmessenger.chat;

import com.example.socketmessenger.auth.handler.JwtHandshakeHandler;
import com.example.socketmessenger.auth.service.JwtService;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * WebSocket 요청을 처리하기 위한 전체 파이프라인
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private final JwtService jwtService;
    private final ChatRoomService chatRoomService;

    public WebSocketServerInitializer(JwtService jwtService, ChatRoomService chatRoomService) {
        this.jwtService = jwtService;
        this.chatRoomService = chatRoomService;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true));
        pipeline.addLast(new JwtHandshakeHandler(jwtService)); // JWT 인증 핸들러
        pipeline.addLast(new RoomHandler(chatRoomService)); // 채팅방 관리 핸들러
        pipeline.addLast(new ChatHandler(chatRoomService)); // 채팅 메세지 관리 핸들러
    }
}
