package org.winry.proto;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.winry.handler.MessageDispatcher;
import org.winry.proto.CommonProtos.ProtoMessage;

public class ProtoMessageInboundHandler extends SimpleChannelInboundHandler<ProtoMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtoMessage msg) {
        Channel channel = ctx.channel();
        MessageDispatcher.dispatch(msg.getCmd(), msg.getData(), channel);
    }
}
