package org.winry.user;

import com.google.protobuf.MessageLite;
import org.winry.pojo.User;

import java.util.List;

public interface UserManager {

    User getUser(int userId);

    List<User> getAllUsers();

    void sendToAll(String cmd, MessageLite message);

}
