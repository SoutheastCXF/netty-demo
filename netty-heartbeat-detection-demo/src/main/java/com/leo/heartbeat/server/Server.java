package com.leo.heartbeat.server;

import com.leo.heartbeat.codec.TinyDecoder;
import com.leo.heartbeat.codec.TinyEncoder;
import com.leo.heartbeat.model.RequestInfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        int port=8081;
        new Server(port).start();
    }


    public void start() throws InterruptedException {
        EventLoopGroup bossGroup=new NioEventLoopGroup(1);
        EventLoopGroup workGroup=new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    /*
                        backlog指定了内核为此套接口排队的最大连接个数
                        对于给定的监听套接口，内核要维护两个队列，未链接队列和已链接队列，，
                        根据TCP三路握手过程中的三次来分隔两个队列
                     */
                    .option(ChannelOption.SO_BACKLOG,128)
                    //.childOption(ChannelOption.TCP_NODELAY,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            /*
                                客户端与服务器编解码的顺序必须相同
                                    这个地方的 必须和服务端对应上。否则无法正常解码和编码
                             */
                            socketChannel.pipeline()
                                    .addLast(new TinyDecoder(RequestInfo.class))
                                    .addLast(new TinyEncoder(RequestInfo.class))
                                    .addLast(new ServerHandler());
                        }
                    });

            ChannelFuture channelFuture=serverBootstrap.bind(port).sync();
            System.out.println(Server.class.getName() + " started and listen on " + channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException e){

        }finally {
            //释放两个IO线程池
            bossGroup.shutdownGracefully().sync();
            workGroup.shutdownGracefully().sync();
        }

    }


}
