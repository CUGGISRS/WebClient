package com.github.wxiaoqi.security.jd.sys.feign;

import com.github.wxiaoqi.security.common.handler.MultipartSupportConfig;
import com.github.wxiaoqi.security.zs.sys.entity.ItemInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目远程接口
 */
@FeignClient(value = "ace-zs-sys", url = "${feign.zsUrl}", configuration = {MultipartSupportConfig.class})
@RequestMapping("ItemInfo")
public interface IItemInfo {

    /**
     * 通过id集合查询数据
     */
    @PostMapping("selectByIds")
    public List<ItemInfo> selectByIds(@RequestBody List<Integer> ids);
}
