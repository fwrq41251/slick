package org.winry.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.winry.pojo.MyMessage;

import java.nio.charset.StandardCharsets;

public class MyMessageEncoder extends MessageToByteEncoder<MyMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MyMessage msg, ByteBuf out) throws Exception {
        byte[] cmd = msg.getCmd().getBytes(StandardCharsets.UTF_8);
        int cmdLength = cmd.length;
        byte[] data = msg.getBytes();
        //todo check dataLength
        int lengthFieldLength = 8;
        int totalLength = cmdLength + data.length + lengthFieldLength;

        out.writeInt(totalLength);
        out.writeInt(cmdLength);
        out.writeBytes(cmd);
        out.writeBytes(data);
    }
}
