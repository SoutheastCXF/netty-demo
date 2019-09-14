package com.leo.heartbeat.client;

import com.leo.heartbeat.codec.TinyDecoder;
import com.leo.heartbeat.codec.TinyEncoder;
import com.leo.heartbeat.model.RequestInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class Client {

    public final String host;
    public final int port;


    public Client(String host, int port) {
        this.host=host;
        this.port=port;
    }

    public static void main(String[] args) throws InterruptedException {
        final String host="127.0.0.1";
        final int port=8081;
        new Client(host,port).start();
    }

    public void start() throws InterruptedException {
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();

        try{
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
//                                    .addLast(new TinyEncoder(RequestInfo.class))
//                                    .addLast(new ClientHandler())
//                                    .addLast(new TinyDecoder(RequestInfo.class));

                                    //上面的排布顺序是有问题的
                                    .addLast(new TinyDecoder(RequestInfo.class))   //extends ChannelInboundHandlerAdapter
                                    .addLast(new TinyEncoder(RequestInfo.class))  //extends ChannelOutboundHandlerAdapter
                                    .addLast(new ClientHandler());

                        }
                    });
            ChannelFuture channelFuture=bootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException ex){

        }finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }

}
