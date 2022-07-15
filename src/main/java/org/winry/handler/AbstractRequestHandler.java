package org.winry.handler;

import com.google.protobuf.MessageLite;
import io.netty.channel.Channel;
import org.winry.pojo.MyMessage;

public abstract class AbstractRequestHandler<T extends MessageLite> {

    private Channel channel;
    private String cmd;

    protected abstract void handle(T t);

    protected void send(MessageLite message) {
        channel.writeAndFlush(toMessage(cmd, message));
    }

    protected void send(String cmd, MessageLite message) {
        channel.writeAndFlush(toMessage(cmd, message));
    }

    private MyMessage toMessage(String cmd, MessageLite message) {
        return new MyMessage(cmd, message.toByteArray());
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
