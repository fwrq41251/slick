package org.winry.user;

import com.google.protobuf.MessageLite;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.winry.pojo.MyMessage;
import org.winry.pojo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultUserManager implements UserManager {

    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final Map<Integer, User> userMap = new ConcurrentHashMap<>();

    public void add(User user) {
        channels.add(user.getChannel());
        userMap.put(user.getUserId(), user);
    }

    public void remove(int userId) {
        User user = getUser(userId);
        channels.remove(user.getChannel());
        userMap.remove(userId);
    }

    @Override
    public User getUser(int userId) {
        return userMap.get(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public void sendToAll(String cmd, MessageLite message) {
        channels.write(new MyMessage(cmd, message));
    }
}
