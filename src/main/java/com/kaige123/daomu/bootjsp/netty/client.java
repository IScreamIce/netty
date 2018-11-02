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
        EventLoopGroup workerGroup = new NioEventLoopGroup();       //创建事件环组，创建连接纽带组

        try {
            Bootstrap b = new Bootstrap();                   //创建服务引导
            b.group(workerGroup);                            //向服务引导中，添加创建的连接纽带进去
            b.channel(NioSocketChannel.class);              
            b.option(ChannelOption.SO_KEEPALIVE, true);      //长时间没有保持心跳，允许发送心跳包
            b.handler(new ChannelInitializer<SocketChannel>() {     //创建添加处理者内部类类
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new clientHandle());      //添加请求处理者
                }
            });

            // 连接发服务器地址
            ChannelFuture f = b.connect(host, port).sync();         //连接的地址与端口
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
