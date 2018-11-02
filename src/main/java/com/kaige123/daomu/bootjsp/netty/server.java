package com.kaige123.daomu.bootjsp.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class server {

    private int port = 8081;

    public void run() throws Exception {
        //bossGroup 用来接收进来的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //workerGroup 用来处理已经被接收的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //启动 NIO 服务的辅助启动类
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new serverHandle());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 服务器绑定端口
            ChannelFuture f = b.bind(port).sync();
            // 等待服务器 socket 关闭 。
            f.channel().closeFuture().sync();
        } finally {
            // 出现异常终止
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            System.out.println("连接关闭等异常");
        }
    }

    public static void main(String[] args) throws Exception {
        new server().run();
    }

}
