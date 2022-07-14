package org.winry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.winry.proto.CommonProtos.ProtoMessage;

import java.net.InetSocketAddress;

public class Slick {

    private final static Logger LOGGER = LoggerFactory.getLogger(Slick.class);

    private int port = 6879;

    public Slick() {
    }

    public Slick port(int port) {
        this.port = port;
        return this;
    }

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new SlickChannelInitializer());
            ChannelFuture f = b.bind().sync();
            f.addListener((ChannelFutureListener) channelFuture -> {
                LOGGER.info("server started");
            });
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                LOGGER.error("Failed to shutdown group", e);
            }
        }
    }

    static class SlickChannelInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline pipeline = socketChannel.pipeline();
            //protobuf decoder
            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
            pipeline.addLast("protobufDecoder", new ProtobufDecoder(ProtoMessage.getDefaultInstance()));

            //protobuf encoder
            pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
            pipeline.addLast("protobufEncoder", new ProtobufEncoder());
        }
    }
}
