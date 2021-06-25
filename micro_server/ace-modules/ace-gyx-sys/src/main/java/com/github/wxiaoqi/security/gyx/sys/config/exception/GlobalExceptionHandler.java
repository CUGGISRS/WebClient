package com.github.wxiaoqi.security.gyx.sys.config.exception;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.info.sys.config.exception.CommonJsonException;
import com.github.wxiaoqi.security.info.sys.utils.CommonUtil;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: hxy
 * @description: 统一异常拦截
 * @date: 2017/10/24 10:31
 */
//gsy 全局异常处理器
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

//    private Logger logger = LoggerFactory.getLogger("GlobalExceptionHandler");

    /**
     * 本系统自定义错误的拦截器
     * 拦截到此错误之后,就返回这个类里面的json给前端
     * 常见使用场景是参数校验失败,抛出此错,返回错误信息给前端
     *
     * @param commonJsonException
     * @return
     * @throws Exception
     */
    @ExceptionHandler(CommonJsonException.class)
    public JSONObject commonJsonExceptionHandler(CommonJsonException commonJsonException) throws Exception {
        return commonJsonException.getResultJson();
    }

    /**
     * 拦截@validate注解异常
     *
     * @return
     * @throws Exception
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JSONObject validationBodyException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        String message = "";
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            if (errors != null) {
                errors.forEach(p -> {
                    FieldError fieldError = (FieldError) p;
                   /* log.error("Data check failure : object{" + fieldError.getObjectName() + "},field{" + fieldError.getField() +
                            "},errorMessage{" + fieldError.getDefaultMessage() + "}");*/
                });
                if (errors.size() > 0) {
                    FieldError fieldError = (FieldError) errors.get(0);
                    message = fieldError.getDefaultMessage();
                }
            }
        }
        return CommonUtil.errorJson(StatusCode.REQUEST_PARAM_ERROR, "".equals(message) ? "请填写正确信息" : message);
    }

    /**
     * SmsException 短信服务自定义异常处理
     */
    @ExceptionHandler(SmsException.class)
    public ObjectRestResponse<Object> smsException(SmsException e) {
        return new ObjectRestResponse<Object>(e.getCodeAndMessage(), e.getData());
    }

    /**
     * SQLException sql异常处理
     */
    /*@ExceptionHandler({SQLException.class})
    public ObjectRestResponse<Object> handleSQLException(SQLException e) {
        return new ObjectRestResponse<Object>(StatusCode.FAIL,"服务运行异常");
    }*/

    /**
     * AccessDeniedException异常处理返回json
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ObjectRestResponse<Exception> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return new ObjectRestResponse<Exception>(StatusCode.FAIL, "不支持当前请求方法", e);
    }
}
