package com.kaige123.daomu.bootjsp.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.*;
import java.util.Arrays;

public class serverHandle extends ChannelInboundHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf result = (ByteBuf) msg;
        byte[] result1 = new byte[result.readableBytes()];

        if (result1.length == 3 || new String(result1).length() == 3) {
            System.out.println("第二次收到客户端请求，来了");
        } else {
            System.out.println("第一次收到客户端请求，我休息比客户端长");
            Thread.sleep(15000);
        }

        System.out.println("---我已经确认收到客户端请求---");

        // msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中
        result.readBytes(result1);
        String resultStr = new String(result1);
        // 释放资源，这行很关键
        result.release();

        FileOutputStream fileOutputStream = new FileOutputStream("D:\\Java_apiCopy.rar", true);
        Savedisk(fileOutputStream, result1);

        sendMsg(ctx);
    }

    String filename;

    // 写入本地磁盘
    public void Savedisk(FileOutputStream fileOutputStream, byte[] bytes) throws IOException {
        fileOutputStream.write(bytes, 0, bytes.length);
        fileOutputStream.close();
    }

    // 向客户端发送消息
    public void sendMsg(ChannelHandlerContext ctx) {
        String response = "OK BYTES Client!";
        // 发送的数据必须转换成ByteBuf字节数据数组,进行传输
        ByteBuf encoded = ctx.alloc().buffer(response.length());
        encoded.writeBytes(response.getBytes());
        ctx.write(encoded);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

}
