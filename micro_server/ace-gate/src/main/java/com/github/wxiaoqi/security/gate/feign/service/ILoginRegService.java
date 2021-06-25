package com.github.wxiaoqi.security.gate.feign.service;

import com.github.wxiaoqi.security.common.vo.UserInfo;
import com.github.wxiaoqi.security.gate.feign.ILoginReg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *登陆注册服务类
 */
@Service
public class ILoginRegService {
    @Autowired
    private ILoginReg iLoginReg;
    /**
     * 通过token获得用户信息
     */
    public UserInfo getUserByToken(String token){
        return iLoginReg.getUserByToken(token);
    }
    /**
     * 判断token是否过期
     */
    public boolean isTokenExpired(String token){
        return iLoginReg.isTokenExpired(token);
    }
}
