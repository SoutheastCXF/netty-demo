package com.uifuture.unpack.protocol;


import lombok.Data;

import java.io.Serializable;

/**
 * 请求数据
 */
@Data
public class Request implements Serializable {
    private static final long serialVersionUID = -2747321595912488569L;
    private Long requestId;
    private String className;
    private String methodName;
    private Class<?> parameterType;
    private Object parameter;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }
}