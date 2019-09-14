package com.uifuture.unpack.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * 响应数据
 */
@Data
public class Response implements Serializable {
    private static final long serialVersionUID = -3136380221020337915L;
    private Long requestId;
    private String error;
    private Object result;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
