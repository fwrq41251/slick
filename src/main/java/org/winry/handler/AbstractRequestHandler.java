package org.winry.handler;

import com.google.protobuf.MessageLite;
import io.netty.channel.ChannelHandlerContext;
import org.winry.pojo.MyMessage;
import org.winry.pojo.User;

public abstract class AbstractRequestHandler<T extends MessageLite> {

    private ChannelHandlerContext ctx;
    private String cmd;

    protected abstract void handle(User user, T t);

    protected void send(MessageLite message) {
        ctx.writeAndFlush(new MyMessage(cmd, message));
    }

    protected void send(String cmd, MessageLite message) {
        ctx.writeAndFlush(new MyMessage(cmd, message));
    }


    public void setChannelHandlerContext(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    protected <S extends MessageLite> void forward(String cmd, S s, User user) {
        MessageDispatcher.dispatch(cmd, s, ctx, user);
    }
}
