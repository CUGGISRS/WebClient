package com.github.wxiaoqi.security.common.msg;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Ace on 2017/6/11.
 */
public class ObjectRestResponse<T> extends BaseResponse {

    T data;
    boolean rel;
    private String msg;
    private String responseTime;
    private int status;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public void setMessage(String message) {
        this.msg = message;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public ObjectRestResponse() {
        this.responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public ObjectRestResponse(StatusCode statusCode, boolean rel,T data) {
        this.status = statusCode.getStatus();
        this.msg = statusCode.getMessage();
        this.data = data;
        this.rel = rel;
        this.responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public ObjectRestResponse(StatusCode statusCode, String msg,T data) {
        this.status = statusCode.getStatus();
        this.msg = msg;
        this.data = data;
        this.rel = false;
        this.responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public boolean isRel() {
        return rel;
    }

    public void setRel(boolean rel) {
        this.rel = rel;
    }

    public ObjectRestResponse(StatusCode statusCode, T data) {
        this.status = statusCode.getStatus();
        this.msg = statusCode.getMessage();
        this.data = data;
        this.responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public ObjectRestResponse(StatusCode statusCode) {
        this.status = statusCode.getStatus();
        this.msg = statusCode.getMessage();
        this.responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public ObjectRestResponse(StatusCode statusCode, T data, boolean rel) {
        this.status = statusCode.getStatus();
        this.msg = statusCode.getMessage();
        this.data = data;
        this.rel = rel;
        this.responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public ObjectRestResponse(StatusCode statusCode, boolean rel) {
        this.status = statusCode.getStatus();
        this.msg = statusCode.getMessage();
        this.rel = rel;
        this.responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public ObjectRestResponse(T data, boolean rel) {
        this.rel = rel;
        this.data = data;
        this.responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public ObjectRestResponse(StatusCode statusCode, String extendMsg) {
        this.status = statusCode.getStatus();
        this.msg = extendMsg;
        this.responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public ObjectRestResponse(StatusCode statusCode, String extendMsg, boolean rel) {
        this.status = statusCode.getStatus();
        this.msg = extendMsg;
        this.rel = rel;
        this.responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public ObjectRestResponse(boolean rel, String msg, T data) {
        this.rel = rel;
        this.msg = msg;
        this.data = data;
        this.responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public ObjectRestResponse rel(boolean rel) {
        this.setRel(rel);
        this.responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return this;
    }


    public ObjectRestResponse data(T data) {
        this.setData(data);
        this.status=super.getStatus();
        this.responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return this;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
