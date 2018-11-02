package com.kaige123.daomu.bootjsp.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class client {

    public void connect(String host, int port) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new clientHandle());
                }
            });

            // 连接发服务器地址
            ChannelFuture f = b.connect(host, port).sync();
            // 关闭连接
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            System.out.println("连接关闭等异常");
        }
    }

    public static void main(String[] args) throws Exception {
        client client = new client();
        client.connect("127.0.0.1", 8081);
    }
}
