/**
 * uifuture.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.uifuture.hearbest.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenhx
 * @version RequestInfo.java, v 0.1 2018-08-10 上午 9:50
 */
@Data
public class RequestInfo implements Serializable {
    /**
     * 以ip为标识
     */
    private String ip;
    private Map<String, Object> cpuPercMap;
    private Map<String, Object> memoryMap;

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setCpuPercMap(Map<String, Object> cpuPercMap) {
        this.cpuPercMap = cpuPercMap;
    }

    public void setMemoryMap(Map<String, Object> memoryMap) {
        this.memoryMap = memoryMap;
    }

    public String getIp() {
        return ip;
    }

    public Map<String, Object> getCpuPercMap() {
        return cpuPercMap;
    }

    public Map<String, Object> getMemoryMap() {
        return memoryMap;
    }
}