package org.winry.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.winry.pojo.MyMessage;

import java.util.List;

public class MyMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int totalLength = in.readInt();
        int cmdLength = in.readInt();
        byte[] cmdBytes = new byte[cmdLength];
        in.readBytes(cmdBytes);
        String cmd = new String(cmdBytes);

        int lengthFieldLength = 8;
        byte[] data = new byte[totalLength - lengthFieldLength - cmdLength];
        in.readBytes(data);

        out.add(new MyMessage(cmd, data));
    }
}
