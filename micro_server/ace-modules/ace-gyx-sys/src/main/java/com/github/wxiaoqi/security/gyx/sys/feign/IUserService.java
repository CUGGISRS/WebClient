package com.github.wxiaoqi.security.gyx.sys.feign;


import com.github.wxiaoqi.security.dzsw.sys.vo.JwtUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "ace-dzsw-sys", url = "${feign.dzUrl}")
@RequestMapping("user")
public interface IUserService {

    @PostMapping("getValueByKey")
    Object getValueByKey();

    @PostMapping("setCacheObject")
    boolean setCacheObject(@RequestParam String key, @RequestParam String value);

    @PostMapping("getUserInfoByToken")
    JwtUser getUserInfoByToken(@RequestParam String token);

}
