package com.github.wxiaoqi.security.info.sys.rest;

import cn.hutool.core.util.PageUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.info.sys.biz.CompanyInfoBiz;
import com.github.wxiaoqi.security.info.sys.entity.CompanyAndFile;
import com.github.wxiaoqi.security.info.sys.entity.CompanyInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = {"企业名录"})
@RestController
@RequestMapping("/company")
@ResponseBody
public class CompanyInfoController {

    @Autowired
    private CompanyInfoBiz companyInfoBiz;

    @ApiOperation(notes = "新增企业名录信息", value = "addNewCompanyInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyName", value = "企业名称", required = true, paramType = "form"),
            @ApiImplicitParam(name = "companyType", value = "企业类型", required = false, paramType = "form"),
            @ApiImplicitParam(name = "chargeMan", value = "负责人", required = false, paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "联系电话", required = false, paramType = "form"),
            @ApiImplicitParam(name = "registMoney", value = "注册资金", required = false, paramType = "form"),
            @ApiImplicitParam(name = "businessCode", value = "营业执照代码", required = false, paramType = "form"),
            @ApiImplicitParam(name = "industry", value = "行业", required = false, paramType = "form"),
            @ApiImplicitParam(name = "introduction", value = "介绍", required = false, paramType = "form"),
            @ApiImplicitParam(name = "place", value = "企业所在地", required = false, paramType = "form"),
            @ApiImplicitParam(name = "files", value = "文件数组", required = false, paramType = "form"),
    })
    @RequestMapping(value = "/addNewCompanyInfo", method = RequestMethod.POST)
    public ObjectRestResponse add(@RequestParam Map<String, String> map, @RequestParam MultipartFile[] files) {
        return companyInfoBiz.addNewCompanyInfo(map, files);
    }

    @ApiOperation(notes = "更新企业名录信息", value = "addNewCompanyInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "form"),
            @ApiImplicitParam(name = "companyName", value = "企业名称", required = true, paramType = "form"),
            @ApiImplicitParam(name = "companyType", value = "企业类型", required = false, paramType = "form"),
            @ApiImplicitParam(name = "chargeMan", value = "负责人", required = false, paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "联系电话", required = false, paramType = "form"),
            @ApiImplicitParam(name = "registMoney", value = "注册资金", required = false, paramType = "form"),
            @ApiImplicitParam(name = "businessCode", value = "营业执照代码", required = false, paramType = "form"),
            @ApiImplicitParam(name = "industry", value = "行业", required = false, paramType = "form"),
            @ApiImplicitParam(name = "introduction", value = "介绍", required = false, paramType = "form"),
            @ApiImplicitParam(name = "place", value = "企业所在地", required = false, paramType = "form"),
            @ApiImplicitParam(name = "deleteIds", value = "删除的图片id数组", required = false, paramType = "form"),
            @ApiImplicitParam(name = "files", value = "文件数组", required = false, paramType = "form"),
    })
    @PutMapping(value = "/updateCompanyInfo")
    public ObjectRestResponse updateCompanyInfo(@RequestParam Integer id,
                                                String companyName,
                                                String companyType,
                                                String chargeMan,
                                                String phone,
                                                String registMoney,
                                                String businessCode,
                                                String industry,
                                                String introduction,
                                                String place,
                                                Integer[] deleteIds,
                                                MultipartFile[] files) {
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setId(id);
        companyInfo.setCompanyName(companyName);
        companyInfo.setCompanyType(companyType);
        companyInfo.setChargeMan(chargeMan);
        companyInfo.setPhone(phone);
        companyInfo.setRegistMoney(registMoney);
        companyInfo.setBusinessCode(businessCode);
        companyInfo.setIndustry(industry);
        companyInfo.setIntroduction(introduction);
        companyInfo.setPlace(place);
        return companyInfoBiz.updateCompanyInfo(companyInfo, deleteIds, files);
    }

//    @ApiOperation(notes = "分页查询企业名录信息", value = "listByPage")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, paramType = "query"),
//    })
//    @RequestMapping(value = "/listByPage", method = RequestMethod.GET)
//    public ObjectRestResponse listByPage(@RequestParam Map<String, Object> map) {
//        return companyInfoBiz.listByPage(map);
//    }

    @ApiOperation(value = "分页查询企业名录(id倒序)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyName", value = "企业名称(like)", required = false, paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", required = false, paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页个数", required = false, paramType = "query"),
    })
    @GetMapping("/allByPage")
    public ObjectRestResponse<JSONObject> all(@RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer size,
                                              @RequestParam(required = false) String companyName) {
        int[] ints = null;
        if (size != null) {
            ints = PageUtil.transToStartEnd(page - 1, size);
            ints[1] = size;
        }
        return companyInfoBiz.all(ints, companyName);
    }

    @ApiOperation(value = "根据id查询单个企业名录详情")
    @GetMapping("/{id}")
    //url:?/1
    public ObjectRestResponse<CompanyAndFile> queryById(@PathVariable Integer id) {
        return companyInfoBiz.queryById(id);
    }

    @ApiOperation(value = "删除:数据库移除")
    @DeleteMapping("/del")
    public ObjectRestResponse<T> delProduct(@RequestParam Integer[] ids) {
        return companyInfoBiz.delCompany(ids);
    }


}
