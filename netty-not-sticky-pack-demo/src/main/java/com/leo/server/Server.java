package com.leo.server;

import com.leo.protocol.Request;
import com.leo.protocol.Response;
import com.leo.protocol.TinyDecoder;
import com.leo.protocol.TinyEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class Server {

    private final int port;

    public Server(final int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        final int port=8081;
        new Server(port).start();
    }

    public void start(){
        EventLoopGroup bossGroup=new NioEventLoopGroup(1);
        EventLoopGroup workerGroup=new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 0))
                                    .addLast(new TinyDecoder(Request.class))
                                    .addLast(new ServerHandler())
                                    .addLast(new TinyEncoder(Response.class));
                        }
                    });

            ChannelFuture channelFuture=serverBootstrap.bind(port).sync();
            System.out.println("serevr is start:"+channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
