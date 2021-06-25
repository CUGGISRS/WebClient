package com.github.wxiaoqi.security.zs.sys.feign;

import com.github.wxiaoqi.security.common.handler.MultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门抽检远程接口
 */
@FeignClient(value = "ace-jd-sys", url = "${feign.jdUrl}", configuration = {MultipartSupportConfig.class})
@RequestMapping("DeptTest")
public interface IDeptTest {

    /**
     * 通过追溯码获得id集合
     */
    @GetMapping("getIdsByTraceCode")
    public List<Integer> getIdsByTraceCode(@RequestParam String traceCode);

    /**
     * 通过追溯码集合获得id集合
     */
    @PostMapping("getIdsByTraceCodes")
    public List<Integer> getIdsByTraceCodes(@RequestBody List<String> traceCodes);

    /**
     * 通过id集合删除数据
     */
    @PostMapping("delByIds")
    public int delByIds(@RequestBody List<Integer> ids);
}
