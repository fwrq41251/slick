package org.winry;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import org.junit.Before;
import org.junit.Test;
import org.winry.handler.AbstractRequestHandler;
import org.winry.handler.MessageDispatcher;
import org.winry.proto.CommonProtos.CommonInteger;
import org.winry.proto.CommonProtos.ProtoMessage;
import org.winry.handler.ProtoMessageInboundHandler;
import org.winry.util.ProtobufUtil;

public class HandlerTest {

    @Before
    public void register() {
        MessageDispatcher.register("test", TestIntegerHandler.class);
    }

    @Test
    public void protoMessageTest() {
        EmbeddedChannel channel = new EmbeddedChannel(new TestChannelInitializer());
        CommonInteger i = CommonInteger.newBuilder().setValue(4).build();
        channel.writeInbound(ProtobufUtil.toMessage("test", i));
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
        protected void initChannel(EmbeddedChannel socketChannel) throws Exception {
            ChannelPipeline pipeline = socketChannel.pipeline();
            //protobuf decoder
            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
            pipeline.addLast("protobufDecoder", new ProtobufDecoder(ProtoMessage.getDefaultInstance()));
            pipeline.addLast("protoMessageInboundHandler", new ProtoMessageInboundHandler());

            //protobuf encoder
            pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
            pipeline.addLast("protobufEncoder", new ProtobufEncoder());
        }
    }
}
