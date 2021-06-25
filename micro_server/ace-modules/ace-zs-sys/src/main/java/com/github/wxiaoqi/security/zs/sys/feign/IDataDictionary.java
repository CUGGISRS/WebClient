package com.github.wxiaoqi.security.zs.sys.feign;

import com.github.wxiaoqi.security.com.sys.entity.DataDictionary;
import com.github.wxiaoqi.security.common.handler.MultipartSupportConfig;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 字典远程接口
 */
@FeignClient(value = "ace-com-sys", url = "${feign.comUrl}", configuration = {MultipartSupportConfig.class})
@RequestMapping("DataDictionary")
public interface IDataDictionary {

    /**
     * 通过条件查询全部数据
     */
    @GetMapping("getAllMayToCondition")
    public ObjectRestResponse<List<DataDictionary>> getAllMayToCondition(
            @RequestParam("type") String type, @RequestParam("remark") String remark,
            @RequestParam("name") String name, @RequestParam("code") String code);
}
