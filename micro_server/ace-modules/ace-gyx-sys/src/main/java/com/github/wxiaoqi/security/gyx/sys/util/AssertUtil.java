package com.github.wxiaoqi.security.gyx.sys.util;


import com.github.wxiaoqi.security.common.exception.BaseException;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.gyx.sys.config.exception.SmsException;


public class AssertUtil {
    public static void isNull(Object object, StatusCode codeAndMessage) {
        if (null == object) {
            throw new SmsException(codeAndMessage, "");
        }
    }
}
