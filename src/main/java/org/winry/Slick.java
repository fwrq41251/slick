package org.winry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.winry.codec.MyMessageDecoder;
import org.winry.codec.MyMessageEncoder;
import org.winry.handler.MyMessageInboundHandler;
import org.winry.util.HandlerAutoRegister;

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

    public Slick registerHandler(String handlerPackage) {
        new HandlerAutoRegister(handlerPackage).register();
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

    /**
     *   <pre>
     *   | Length | CmdLength | Cmd  | Data           |
     *   |--------|-----------|------|----------------|
     *   | 0x000E | 0x0010    | "my" | "HELLO, WORLD" |
     *   </pre>
     */
    static class SlickChannelInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) {
            ChannelPipeline pipeline = socketChannel.pipeline();

            pipeline.addLast("myMessageDecoder", new MyMessageDecoder());
            pipeline.addLast("protoMessageInboundHandler", new MyMessageInboundHandler());

            pipeline.addLast("myMessageEncoder", new MyMessageEncoder());
        }
    }
}
