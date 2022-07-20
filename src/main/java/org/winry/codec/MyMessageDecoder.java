package org.winry.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import org.winry.pojo.MyMessage;

import java.util.List;

public class MyMessageDecoder extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int lengthFieldLength = 8;
        if (in.readableBytes() < lengthFieldLength) {
            return;
        }

        int offset = 0;
        int totalLength = in.getInt(offset);

        if (totalLength < 0) {
            throw new CorruptedFrameException(
                    "negative length field: " + totalLength);
        }

        if (in.readableBytes() < totalLength) {
            return;
        }

        in.skipBytes(4);
        int cmdLength = in.readInt();
        byte[] cmdBytes = new byte[cmdLength];
        in.readBytes(cmdBytes);
        String cmd = new String(cmdBytes);

        byte[] data = new byte[totalLength - lengthFieldLength - cmdLength];
        in.readBytes(data);

        out.add(new MyMessage(cmd, data));
    }
}
