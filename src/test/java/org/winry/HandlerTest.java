package org.winry;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Before;
import org.junit.Test;
import org.winry.codec.MyMessageDecoder;
import org.winry.codec.MyMessageEncoder;
import org.winry.handler.AbstractRequestHandler;
import org.winry.handler.MessageDispatcher;
import org.winry.handler.MyMessageInboundHandler;
import org.winry.proto.CommonProtos.CommonInteger;

import java.nio.charset.StandardCharsets;

public class HandlerTest {

    @Before
    public void register() {
        MessageDispatcher.register("test", TestIntegerHandler.class);
    }

    @Test
    public void protoMessageTest() {
        EmbeddedChannel channel = new EmbeddedChannel(new TestChannelInitializer());
        CommonInteger i = CommonInteger.newBuilder().setValue(4).build();

        ByteBuf byteBuf = Unpooled.buffer();
        byte[] cmd = "test".getBytes(StandardCharsets.UTF_8);
        int cmdLength = cmd.length;
        byte[] data = i.toByteArray();
        int lengthFieldLength = 8;
        int totalLength = cmdLength + data.length + lengthFieldLength;

        byteBuf.writeInt(totalLength);
        byteBuf.writeInt(cmdLength);
        byteBuf.writeBytes(cmd);
        byteBuf.writeBytes(data);

        channel.writeInbound(byteBuf);
        channel.finish();
    }

    public static class TestIntegerHandler extends AbstractRequestHandler<CommonInteger> {

        @Override
        protected void handle(CommonInteger commonInteger) {
            System.out.println(commonInteger.getValue());
        }
    }

    public static class TestChannelInitializer extends ChannelInitializer<EmbeddedChannel> {

        @Override
        protected void initChannel(EmbeddedChannel socketChannel) {
            ChannelPipeline pipeline = socketChannel.pipeline();

            pipeline.addLast("myMessageDecoder", new MyMessageDecoder());
            pipeline.addLast("protoMessageInboundHandler", new MyMessageInboundHandler());

            pipeline.addLast("myMessageEncoder", new MyMessageEncoder());
        }
    }
}
