package org.winry.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.winry.proto.CommonProtos.ProtoMessage;

public class ProtoMessageInboundHandler extends SimpleChannelInboundHandler<ProtoMessage> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProtoMessageInboundHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtoMessage msg) {
        Channel channel = ctx.channel();
        MessageDispatcher.dispatch(msg.getCmd(), msg.getData(), channel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("Error handle request", cause);
    }
}
