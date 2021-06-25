package com.github.wxiaoqi.security.com.sys.feign;

import com.github.wxiaoqi.security.common.handler.MultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 企业_用户远程接口
 */
@FeignClient(value = "ace-zs-sys", url = "${feign.zsUrl}", configuration = {MultipartSupportConfig.class})
@RequestMapping("BetweenEnterpriseUser")
public interface IBetweenEnterpriseUser {

    /**
     * 通过用户id删除数据
     */
    @DeleteMapping("delByUserId")
    public int delByUserId(@RequestParam Integer userId);

    /**
     * 通过用户id集合删除数据
     */
    @PostMapping("delByUserIds")
    public int delByUserIds(@RequestBody List<Integer> userIds);


}
