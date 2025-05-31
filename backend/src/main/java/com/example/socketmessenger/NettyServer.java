package com.example.socketmessenger;

import com.example.socketmessenger.auth.service.JwtService;
import com.example.socketmessenger.chat.AuthHandler;
import com.example.socketmessenger.chat.ChatHandler;
import com.example.socketmessenger.config.RedisPublisher;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class NettyServer {

    private final JwtService jwtService;
    private final RedisPublisher redisPublisher;

    public NettyServer(JwtService jwtService, RedisPublisher redisPublisher) {
        this.jwtService = jwtService;
        this.redisPublisher = redisPublisher;
    }

    public void start() throws Exception {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new HttpServerCodec());
                        ch.pipeline().addLast(new HttpObjectAggregator(65536));
                        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));
                        ch.pipeline().addLast(new AuthHandler(jwtService)); // JWT 인증 처리
                        ch.pipeline().addLast(new ChatHandler()); // 메시지 처리
                    }
                });

        bootstrap.bind(9090).sync();
    }
}
