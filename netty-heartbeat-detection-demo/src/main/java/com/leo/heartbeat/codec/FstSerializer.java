package com.leo.heartbeat.codec;

import org.nustaq.serialization.FSTConfiguration;

public class FstSerializer {

    private static FSTConfiguration fstConfiguration=FSTConfiguration.createDefaultConfiguration();

    /**
     * 反序列化
     */
    public static <T> T deserialize(byte[] data,Class<T> clazz){
        return (T)fstConfiguration.asObject(data);
    }

    /**
     * 序列化
     */
    public static <T> byte[] serialize(T obj){
        return fstConfiguration.asByteArray(obj);
    }
}
