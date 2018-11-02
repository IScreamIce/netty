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
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();   //事件环组,连接纽带,在纽带上相互通讯
        try {
            //启动 NIO 服务的辅助启动类
            ServerBootstrap b = new ServerBootstrap();      //服务引导
            b.group(bossGroup, workerGroup)                 //向引导添加纽带
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {     //继承添加请求处理者类
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new serverHandle());          //添加处理者
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)           //最大的连接数
                    .childOption(ChannelOption.SO_KEEPALIVE, true);  //长时间未心跳,允许发送探测包,探测情况
            
            ChannelFuture f = b.bind(port).sync();          // 服务器绑定端口
            f.channel().closeFuture().sync();
        } finally {
            // 出现异常终止
            workerGroup.shutdownGracefully();               //关闭连接
            bossGroup.shutdownGracefully();
            System.out.println("连接关闭等异常");
        }
    }

    public static void main(String[] args) throws Exception {
        new server().run();
    }

}
