package com.example.socketmessenger;

import com.example.socketmessenger.auth.service.JwtService;
import com.example.socketmessenger.chat.ChatRoomService;
import com.example.socketmessenger.chat.WebSocketServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    private final JwtService jwtService;
    private final ChatRoomService chatRoomService;

    public NettyServer(JwtService jwtService, ChatRoomService chatRoomService) {
        this.jwtService = jwtService;
        this.chatRoomService = chatRoomService;
    }

    public void start() throws Exception {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WebSocketServerInitializer(jwtService, chatRoomService));

        bootstrap.bind(9090).sync();
    }
}
