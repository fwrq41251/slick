package org.winry.handler;

import com.google.protobuf.MessageLite;
import io.netty.channel.Channel;
import org.winry.util.ProtobufUtil;

public abstract class AbstractRequestHandler<T extends MessageLite> {

    private Channel channel;
    private String cmd;

    protected abstract void handle(T t);

    protected void send(MessageLite message) {
        channel.writeAndFlush(ProtobufUtil.toMessage(this.cmd, message));
    }

    protected void send(String cmd, MessageLite message) {
        channel.writeAndFlush(ProtobufUtil.toMessage(cmd, message));
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
