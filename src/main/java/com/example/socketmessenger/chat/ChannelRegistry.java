package com.example.socketmessenger.chat;

import io.netty.channel.Channel;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelRegistry {

    private static final Set<Channel> channels = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static void add(Channel channel) {
        channels.add(channel);
    }

    public static void remove(Channel channel) {
        channels.remove(channel);
    }

    public static Set<Channel> getAllChannels() {
        return channels;
    }
}
