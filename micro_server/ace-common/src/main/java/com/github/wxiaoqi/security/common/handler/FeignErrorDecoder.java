package com.github.wxiaoqi.security.common.handler;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.constant.CommonConstants;
import com.github.wxiaoqi.security.common.exception.BaseException;
import com.github.wxiaoqi.security.common.msg.BaseResponse;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.json.JSONException;

import java.io.IOException;
import java.io.Reader;

/**
 * feign调用方抛出异常与服务方抛出异常一致
 * @controllerAdvice或者HandlerExceptionResolver是不能直接捕获到FeignException,
 * 所以需要在Feign层面拿到具体异常重新封装，最后把cloud service内部的异常安全(一样的错误码 、 一样的错误信息)送给了client。
 */
public class FeignErrorDecoder  implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {

        try {
            Response.Body body=  response.body();
            Reader reader= body.asReader();
          String  message= Util.toString(reader);
          if(message!=null){
              JSONObject jsonObject = JSONObject.parseObject(message);
           return new BaseException(jsonObject.getString(CommonConstants.RESP_MESSAGE),
                     jsonObject.getInteger(CommonConstants.RESP_STATUS));
          }
        } catch (IOException ioE) {
            ioE.printStackTrace();
        }
        return new BaseException("IOException导致无法读出"+methodKey+"的异常信息",CommonConstants.EX_OTHER_CODE);
    }
}
