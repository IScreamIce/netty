package com.kaige123.daomu.bootjsp.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.FileInputStream;

public class clientHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端收到处理");
        ByteBuf result = (ByteBuf) msg;
        byte[] result1 = new byte[result.readableBytes()];
        result.readBytes(result1);
        System.out.println("收到数据:" + result1 + "收到长度: " + result1.length);
        result.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }


    // 连接成功后，向server发送消息
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        ByteBuf encoded1 = ctx.alloc().buffer(8);
        encoded1.writeBytes("xixihaha".getBytes(), 0, 8);
        ctx.write(encoded1);
        ctx.flush();

        Thread.sleep(10000);
        System.out.println("我已经休息完成了！！");

        ByteBuf encoded = ctx.alloc().buffer(3);
        encoded.writeBytes("123".getBytes(), 0, 3);
        ctx.write(encoded);
        ctx.flush();

    }
}
