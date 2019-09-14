package com.leo.heartbeat.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TinyDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

    public TinyDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int size=byteBuf.readableBytes();
        byte[] bytes=new byte[size];
        byteBuf.readBytes(bytes);
        Object obj=FstSerializer.deserialize(bytes,genericClass);
        list.add(obj);
    }
}
