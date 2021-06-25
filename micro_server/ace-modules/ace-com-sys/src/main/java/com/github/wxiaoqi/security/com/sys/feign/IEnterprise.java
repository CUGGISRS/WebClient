package com.github.wxiaoqi.security.com.sys.feign;

import com.github.wxiaoqi.security.common.handler.MultipartSupportConfig;
import com.github.wxiaoqi.security.common.vo.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 企业远程接口
 */
@FeignClient(value = "ace-zs-sys", url = "${feign.zsUrl}", configuration = {MultipartSupportConfig.class})
@RequestMapping("Enterprise")
public interface IEnterprise {

    /**
     * 通过用户id获得带有企业信息的bean
     */
    @GetMapping("getOneByUserId")
    public UserInfo getOneByUserId(@RequestParam("userId") Integer userId);

}
