package org.winry.pojo;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class User {

    private int userId;
    private Channel channel;
    private final Map<String, Object> propertyMap = new ConcurrentHashMap<>();

    public User(int userId, Channel channel) {
        this.userId = userId;
        this.channel = channel;
    }

    public int getUserId() {
        return userId;
    }
    public Channel getChannel() {
        return channel;
    }
    public <T> void setProperty(String key, T value) {
        propertyMap.put(key, value);
    }
    public <T> T getProperty(String key) {
        return (T) propertyMap.get(key);
    }
}
