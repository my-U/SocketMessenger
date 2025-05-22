package com.example.socketmessenger.chat;

import com.example.socketmessenger.config.RedisPublisher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final RedisPublisher redisPublisher;

    public ChatHandler(RedisPublisher redisPublisher) {
        this.redisPublisher = redisPublisher;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String userId = (String) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        String message = msg.text();

        redisPublisher.publish("chat", userId + ": " + message);
    }
}
