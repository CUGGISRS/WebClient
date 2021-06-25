package com.github.wxiaoqi.security.gate.feign;

import com.github.wxiaoqi.security.common.handler.MultipartSupportConfig;
import com.github.wxiaoqi.security.common.vo.UserInfo;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 登陆注册远程接口
 */
@RefreshScope
@FeignClient(value = "ace-com-sys",url = "${feign.comUrl}",configuration ={MultipartSupportConfig.class})
public interface ILoginReg {


    /**
     * 通过token获得用户信息
     */
    @GetMapping("/getUserByToken")
    public UserInfo getUserByToken(@RequestParam("token") String token);

    /**
     * 判断token是否过期
     */
    @GetMapping("isTokenExpired")
    public boolean isTokenExpired(@RequestParam("token") String token);
}
