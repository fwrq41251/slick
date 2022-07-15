package org.winry.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.winry.pojo.MyMessage;

public class MyMessageInboundHandler extends SimpleChannelInboundHandler<MyMessage> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MyMessageInboundHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessage msg) {
        Channel channel = ctx.channel();
        MessageDispatcher.dispatch(msg.getCmd(), msg.getBytes(), channel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("Error handle request", cause);
    }
}
