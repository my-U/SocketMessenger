package com.example.socketmessenger.chat;

import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatRoomService {

    /** 채팅방 자체의 메타데이터를 저장. <key:value> - <roomId:ChatRoom> **/
    private final Map<String, ChatRoom> chatRoomMap = new ConcurrentHashMap<>();

    /** 각 채팅방에 접속한 모든 사용자들의 WebSocket 채널 목록을 저장. <key:value> - <roomId:Set<Channel>> **/
    private final Map<String, Set<Channel>> roomChannelMap = new ConcurrentHashMap<>();

    /** 특정 유저가 특정 방에 어떤 Channel로 접속했는지 1:1로 저장, 중복 접속 방지 위해 사용. <key:value> - <<roomId:userId>:Channel> **/
    private static final Map<String, Channel> userRoomChannelMap = new ConcurrentHashMap<>();

    public static final Map<String, Set<Channel>> roomMap = new ConcurrentHashMap<>();

    /**
     * 모든 채팅방 조회
     */
    public List<ChatRoom> getAllRooms() {
        return new ArrayList<>(chatRoomMap.values());
    }

    /**
     * 새로운 채팅방 생성
     */
    public ChatRoom createRoom(String roomName) {
        String roomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = new ChatRoom(roomId, roomName);
        chatRoomMap.put(roomId, chatRoom);
        roomChannelMap.put(roomId, ConcurrentHashMap.newKeySet());
        return chatRoom;
    }

    /**
     * 채팅방 존재 여부 확인
     */
    public boolean exists(String roomId) {
        return chatRoomMap.containsKey(roomId);
    }

    /** 연결 소켓 등록 **/
    public void addUserChannel(String roomId, String accountId, Channel channel) {
        String key = roomId + ":" + accountId;

        Channel existing = userRoomChannelMap.get(key);
        if (existing != null && existing != channel) {
            existing.close(); // 중복 제거
        }

        userRoomChannelMap.put(key, channel);
        roomChannelMap.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(channel);
    }

    /** 연결 소켓 제거 **/
    public void removeUserChannel(String roomId, String accountId, Channel channel) {
        String key = roomId + ":" + accountId;
        userRoomChannelMap.remove(key);

        Set<Channel> room = roomChannelMap.get(roomId);
        if (room != null) {
            room.remove(channel);
            if (room.isEmpty()) {
                roomChannelMap.remove(roomId);
            }
        }
    }

    /**
     * 특정 채팅방(roomId)에 연결된 소켓(Channel)들의 집합(Set)을 조회
     */
    public Set<Channel> getChannelSet(String roomId) {
        return roomChannelMap.getOrDefault(roomId, Collections.emptySet());
    }
}
