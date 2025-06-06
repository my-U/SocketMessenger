package com.example.socketmessenger.chat;

import lombok.Getter;

@Getter
public class ChatRoom {
    private final String roomId;
    private final String roomName;

    public ChatRoom(String roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }


}