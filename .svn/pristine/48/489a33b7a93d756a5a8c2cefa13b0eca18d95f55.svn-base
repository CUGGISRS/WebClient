package com.github.wxiaoqi.security.com.sys.feign.service;

import com.github.wxiaoqi.security.com.sys.feign.IEnterprise;
import com.github.wxiaoqi.security.common.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 企业服务类
 */
@Service
public class IEnterpriseService {
    @Autowired
    private IEnterprise iEnterprise;

    /**
     * 通过用户id获得企业信息的bean
     */
    public UserInfo getOneByUserId(Integer userId) {
        return iEnterprise.getOneByUserId(userId);
    }
}
