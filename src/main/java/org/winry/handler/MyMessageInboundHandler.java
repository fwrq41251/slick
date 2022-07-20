package org.winry.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.winry.Slick;
import org.winry.pojo.MyMessage;
import org.winry.pojo.User;
import org.winry.user.DefaultUserManager;

public class MyMessageInboundHandler extends SimpleChannelInboundHandler<MyMessage> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MyMessageInboundHandler.class);
    private User user;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessage msg) {
        MessageDispatcher.dispatch(msg.getCmd(), msg.getBytes(), ctx, user);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("Error handle request", cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                ctx.close();
                ((DefaultUserManager) Slick.userManager()).remove(user.getUserId());
            }
        }
    }
}
