package org.winry.handler;

import com.google.protobuf.MessageLite;
import org.winry.pojo.User;

public interface RequestHandler {

    void send(MessageLite message);

    void send(String cmd, MessageLite message);

    <S extends MessageLite> void forward(String cmd, S s, User user);
}
