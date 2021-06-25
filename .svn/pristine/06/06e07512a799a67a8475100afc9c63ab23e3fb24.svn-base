package com.zd.earth.manage.config;

import com.zd.earth.manage.msg.ObjectRestResponse;
import com.zd.earth.manage.msg.StatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.io.EOFException;
import java.net.SocketTimeoutException;

/**
 * 全局统一异常处理  经过测试，被要求单独处理的异常会先被处理，而后其他异常会被Exception（默认形式）的处理方法捕获
 */
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * 未知异常
     */
    @ExceptionHandler
    public ObjectRestResponse handleException(Exception ex) {
        return new ObjectRestResponse(StatusCode.FAIL,"未知异常："+ex.getMessage());
    }


    /**
     * MultipartException 异常
     */
    @ExceptionHandler(value = MultipartException.class)
    public ObjectRestResponse handleMultipartException(MultipartException ex) {
        Throwable e= ex.getRootCause();
        String msg;
        if(e!=null){
            if(e instanceof EOFException){
                msg="EOFException异常："+e.getMessage();
            }else if(e instanceof SocketTimeoutException){
                msg="SocketTimeoutException异常："+e.getMessage();
            }else {
                msg="未知MultipartException异常："+e.getMessage();
            }
        }else {
            msg="MultipartException异常："+ex.getMessage();
        }
       return new ObjectRestResponse(StatusCode.FAIL,msg);
    }



}
