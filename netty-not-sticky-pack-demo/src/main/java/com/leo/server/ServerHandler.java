package com.leo.server;

import com.leo.protocol.Request;
import com.leo.protocol.Response;
import com.uifuture.unpack.model.User;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<Request> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, final Request request) throws Exception {
        System.out.println("服务器接收到的消息："+request);
        Response response = new Response();
        response.setRequestId(2L);
        User user = new User();
        user.setUsername("测试");
        user.setPassword("1234");
        user.setAge(21);
        response.setResult(user);

        //addListener是非阻塞的，异步执行。它会把特定的ChannelFutureListener添加到ChannelFuture中，然后I/O线程会在I/O操作相关的future完成的时候通知监听器。
        ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("接口请求IP:" + request.getRequestId());
            }
        });
    }
}
