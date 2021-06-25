package com.github.wxiaoqi.security.gyx.sys.config.exception;

import com.github.wxiaoqi.security.common.msg.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王军伟
 * @date 2020/5/20 下午7:25
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsException extends RuntimeException {

    private static final long serialVersionUID = 790952055279725516L;

    private StatusCode codeAndMessage;
    private Object data;
}
