package com.leo.heartbeat.server;

import com.leo.heartbeat.model.RequestInfo;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerHandler extends SimpleChannelInboundHandler {

    private static final String SUCCESS_KEY="auth_success_key";
    private static final Map<String,String> AUTH_IP_MAP=new HashMap<>();
    private static final Set<String> AUTH_IP_SET=new HashSet<>();

    static {
        AUTH_IP_MAP.put("169.254.196.121", "1234");
    }

    private boolean auth(String o,ChannelHandlerContext ctx){
        String[] res=o.split(",");
        String auth=AUTH_IP_MAP.get(res[0]);
        if(auth!=null&&auth.equals(res[1])){
            ctx.writeAndFlush(SUCCESS_KEY);
            AUTH_IP_SET.add(res[0]);
            return true;
        }
        ctx.writeAndFlush("auth failure!").addListener(ChannelFutureListener.CLOSE);
        return false;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if(o instanceof String){
            auth((String)o,channelHandlerContext);
        }else if(o instanceof RequestInfo){
            RequestInfo info=(RequestInfo)o;
            if(!AUTH_IP_SET.contains(info.getIp())){
                System.out.println("尚未认证的机器..."+info.getIp());
                return;
            }

            System.out.println("--------------------" + System.currentTimeMillis() + "------------------------");
            System.out.println("当前主机ip为: " + info.getIp());
            System.out.println("当前主机cpu情况: ");
            Map<String, Object> cpu = info.getCpuPercMap();
            System.out.println("总使用率: " + cpu.get("combined"));
            System.out.println("用户使用率: " + cpu.get("user"));
            System.out.println("系统使用率: " + cpu.get("sys"));
            System.out.println("等待率: " + cpu.get("wait"));
            System.out.println("空闲率: " + cpu.get("idle"));

            System.out.println("当前主机memory情况: ");
            Map<String, Object> memory = info.getMemoryMap();
            System.out.println("内存总量: " + memory.get("total"));
            System.out.println("当前内存使用量: " + memory.get("used"));
            System.out.println("当前内存剩余量: " + memory.get("free"));
            System.out.println("--------------------------------------------");

            channelHandlerContext.writeAndFlush("success");
        }else{
            channelHandlerContext.writeAndFlush("error").addListener(ChannelFutureListener.CLOSE);
        }
    }
}
