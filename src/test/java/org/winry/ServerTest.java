package org.winry;

import org.junit.Test;

public class ServerTest {

//    public void startClient() {
//        EventLoopGroup group = new NioEventLoopGroup();
//        try {
//            Bootstrap b = new Bootstrap();
//            b.group(group)
//                    .channel(NioSocketChannel.class)
//                    .remoteAddress(new InetSocketAddress("127.0.0.1", 6879))
//                    .handler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        public void initChannel(SocketChannel ch) {
//                            ch.pipeline().addLast(
//                                    new simpleha());
//                        }
//                    });
//            ChannelFuture f = b.connect().sync();
//            f.channel().closeFuture().sync();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } finally {
//            try {
//                group.shutdownGracefully().sync();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Test
    public void startServer() {
        new Slick().start();
    }
}
