package com.github.wxiaoqi.security.dzsw.sys.utils.sms;


import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.dzsw.sys.config.exception.SmsException;


public class AssertUtil {
    public static void isNull(Object object, StatusCode codeAndMessage) {
        if (null == object) {
            throw new SmsException(codeAndMessage, "");
        }
    }
}
