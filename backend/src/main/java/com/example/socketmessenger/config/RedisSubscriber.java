package com.example.socketmessenger.config;

import com.example.socketmessenger.chat.ChannelRegistry;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.nio.charset.StandardCharsets;

public class RedisSubscriber implements MessageListener { // Redis 채널을 구독하여 메시지를 수신

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);

        // WebSocket 연결된 클라이언트 채널들에 브로드캐스트
        for (Channel ch : ChannelRegistry.getAllChannels()) {
            ch.writeAndFlush(new TextWebSocketFrame(payload));
        }
    }
}