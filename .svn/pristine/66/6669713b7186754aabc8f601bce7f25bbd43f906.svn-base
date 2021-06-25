package com.github.wxiaoqi.security.jd.sys.feign;

import com.github.wxiaoqi.security.common.handler.MultipartSupportConfig;
import com.github.wxiaoqi.security.zs.sys.entity.BetweenTestItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检验_项目远程接口
 */
@FeignClient(value = "ace-zs-sys", url = "${feign.zsUrl}", configuration = {MultipartSupportConfig.class})
@RequestMapping("BetweenTestItem")
public interface IBetweenTestItem {

    /**
     * 通过检验表id和类型获得数据
     */
    @GetMapping("getByTIdAndTType")
    public List<BetweenTestItem> getByTIdAndTType(@RequestParam("testId") Integer testId,
                                                  @RequestParam("testType") String testType);

    /**
     * 通过检验表id集合和类型获得数据
     */
    @PostMapping("getByTIdsAndTType")
    public List<BetweenTestItem> getByTIdsAndTType(@RequestBody List<Integer> testIds,
                                                   @RequestParam("testType") String testType);

    /**
     * 通过id集合删除数据
     */
    @PostMapping("deleteByIds")
    public Integer deleteByIds(@RequestBody List<Integer> ids);
}
