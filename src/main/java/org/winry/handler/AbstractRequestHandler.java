package org.winry.handler;

import com.google.protobuf.MessageLite;
import io.netty.channel.Channel;

public abstract class AbstractRequestHandler<T extends MessageLite> {

    private final Channel channel;

    protected AbstractRequestHandler(Channel channel) {
        this.channel = channel;
    }

    abstract void handle(T t);

    protected void send(MessageLite message) {
    }

    protected void send(String cmd, MessageLite message) {

    }
}
