package com.github.wxiaoqi.security.dzsw.sys.service;

import com.github.wxiaoqi.security.dzsw.sys.vo.SendSmsDto;
import com.github.wxiaoqi.security.dzsw.sys.vo.VerifyCodeDto;

public interface SendSmsService {
    /**
     * 发送短信的接口
     *
     * @param sendSmsDto
     * @return
     */
    boolean sendSms(SendSmsDto sendSmsDto);

    /**
     * 验证短信是否正确
     *
     * @param verifyCodeDto
     * @return
     */
    void verifyCode(VerifyCodeDto verifyCodeDto);

}
