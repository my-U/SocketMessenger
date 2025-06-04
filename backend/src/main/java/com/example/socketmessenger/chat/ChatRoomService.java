package com.example.socketmessenger.chat;

import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatRoomService {

    private final Map<String, ChatRoom> chatRoomMap = new ConcurrentHashMap<>();
    private final Map<String, Set<Channel>> roomChannelMap = new ConcurrentHashMap<>();

    /**
     * 모든 채팅방 조회
     */
    public List<ChatRoom> getAllRooms() {
        return new ArrayList<>(chatRoomMap.values());
    }

    /**
     * 새로운 채팅방 생성
     */
    public String createRoom(String roomName) {
        String roomId = UUID.randomUUID().toString();
        chatRoomMap.put(roomId, new ChatRoom(roomId, roomName));
        roomChannelMap.put(roomId, ConcurrentHashMap.newKeySet());
        return roomId;
    }

    /**
     * 채팅방 존재 여부 확인
     */
    public boolean exists(String roomId) {
        return chatRoomMap.containsKey(roomId);
    }
}
