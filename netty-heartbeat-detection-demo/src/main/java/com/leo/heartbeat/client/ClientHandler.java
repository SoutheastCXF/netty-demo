package com.leo.heartbeat.client;

import com.leo.heartbeat.model.RequestInfo;
import com.uifuture.hearbest.utils.SigarUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ClientHandler extends SimpleChannelInboundHandler {

    private static final String SUCCESS_KEY="auth_success_key";
    /**
     * 开一个线程进行心跳包
     */
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    /**
     * 定时任务
     */
    private ScheduledFuture<?> heartBeat;
    /**
     * 主动向服务器发送认证信息
     */
    private InetAddress addr;


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        addr=InetAddress.getLocalHost();
        System.out.println("addr=" + addr);
        String ip = "169.254.196.121";
        String key = "1234";
        // 证书
        String auth=ip+","+key;

        ctx.writeAndFlush(auth);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        try{
            if(o instanceof String){
                String ret=(String)o;
                if(SUCCESS_KEY.equals(ret)){
                    //收到认证 确认信息，设置每隔5秒发送心跳消息
                    this.heartBeat=this.scheduler.scheduleWithFixedDelay(new HeartBeat(channelHandlerContext),0,5, TimeUnit.SECONDS);
                    System.out.println("接收到信息："+o);
                }
                else{
                    //收到心跳包，确认信息
                    System.out.println("接收到信息："+o);
                }
            }
        }finally {
            // 只读, 需要手动释放引用计数
            ReferenceCountUtil.release(o);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //记录错误日志，并关闭channel
        cause.printStackTrace();
        ctx.close();
    }

    private class HeartBeat implements Runnable{

        private final ChannelHandlerContext ctx;

        private int times=0;

        public HeartBeat(final ChannelHandlerContext context) {
            this.ctx = context;
        }

        @Override
        public void run() {
            try{
                if(times++>10){
                    closeHeartBeat();
                    return;
                }
                System.out.println("第"+times+"次请求...");
                RequestInfo info=new RequestInfo();
                //ip

                info.setIp(addr.getHostAddress());
                Sigar sigar= SigarUtil.getInstance();
                //cpu prec
                CpuPerc cpuPerc=sigar.getCpuPerc();
                Map<String, Object> cpuPercMap = new HashMap<>();
                cpuPercMap.put("combined", cpuPerc.getCombined());
                cpuPercMap.put("user", cpuPerc.getUser());
                cpuPercMap.put("sys", cpuPerc.getSys());
                cpuPercMap.put("wait", cpuPerc.getWait());
                cpuPercMap.put("idle", cpuPerc.getIdle());
                // memory
                Mem mem = sigar.getMem();
                Map<String, Object> memoryMap = new HashMap<>();
                memoryMap.put("total", mem.getTotal() / 1024L / 1024L);
                memoryMap.put("used", mem.getUsed() / 1024L / 1024L);
                memoryMap.put("free", mem.getFree() / 1024L / 1024L);

                info.setCpuPercMap(cpuPercMap);
                info.setMemoryMap(memoryMap);
                ctx.writeAndFlush(info);
            }catch (SigarException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *取消定时任务
     */
    public void closeHeartBeat(){
        if(heartBeat!=null){
            heartBeat.cancel(true);
            heartBeat=null;
        }
    }

}
