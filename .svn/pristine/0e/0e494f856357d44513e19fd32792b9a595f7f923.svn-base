package com.github.wxiaoqi.security.jd.sys.feign;

import com.alibaba.fastjson.JSONArray;
import com.github.wxiaoqi.security.common.handler.MultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 产品远程接口
 */
@FeignClient(value = "ace-zs-sys", url = "${feign.zsUrl}", configuration = {MultipartSupportConfig.class})
@RequestMapping("Product")
public interface IProduct {

    /**
     * 获得某一企业(某一基地类型)的产品批次号集合
     */
    @GetMapping("getPBNByEnterpriseId")
    public List<String> getPBNByEnterpriseId(@RequestParam("enterpriseId") Integer enterpriseId,
                                             @RequestParam("baseType") String baseType);

    /**
     * 将企业分区域后存入map
     */
    @PostMapping("enterpriseArea")
    public Map<String, List<Integer>> enterpriseArea(@RequestBody List<Integer> eIds);

    /**
     * 将产品详情分水产、种植模块后存入map
     */
    @PostMapping("productDetailModule")
    public Map<String, List<Integer>> productDetailModule(@RequestBody List<Integer> pdIds);

    /**
     * 通过产品详情id获得所属系统模块
     */
    @GetMapping("getSysModuleByPDId")
    public Integer getSysModuleByPDId(@RequestParam("pdId") Integer pdId);


    /**
     * 获得这些加工产品追溯码的预警相关信息
     */
    @PostMapping("pbnPartMap")
    public JSONArray pbnPartMap(@RequestBody List<String> pbnList);

}
