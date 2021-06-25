package com.github.wxiaoqi.security.gyx.sys.rest;

import cn.hutool.core.util.PageUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.gyx.sys.biz.ExpertInfoBiz;
import com.github.wxiaoqi.security.gyx.sys.entity.ExpertInfo;
import io.swagger.annotations.*;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Api(tags = {"专家信息"})
@RestController
@RequestMapping("expert")
public class ExpertInfoController {

    @Resource
    protected ExpertInfoBiz expertInfoBiz;

    @ApiOperation(value = "分页条件查询专家列表,条件：serviceArea 所在地区")
    @GetMapping("/getExpertInfoList")
    public ObjectRestResponse<JSONObject> getExpertInfoList( Integer page,  Integer limit,
                                                             String serviceArea,
                                                             String expertName,
                                                             String expertise) {
        int[] ints = PageUtil.transToStartEnd(page - 1, limit);
        ints[1] = limit;
        return expertInfoBiz.getExpertInfoList(ints, serviceArea,expertName,expertise);
    }

//    @GetMapping("/getExpertInfoListByPage")
//    public ObjectRestResponse<JSONObject> getExpertInfoListByPage(@RequestParam Map<String, Object> params) {
//        return expertInfoBiz.getExpertInfoListByPage(params);
//    }

    @ApiOperation(value = "查询单条数据")
    @GetMapping("/getExpert/{id}")
    public ObjectRestResponse<ExpertInfo> getOne(@PathVariable Integer id) {
        return expertInfoBiz.getOne(id);
    }

    @ApiOperation(value = "新增专家信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "expertName", value = "名称", required = true, paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "电话", required = false, paramType = "form"),
            @ApiImplicitParam(name = "wechat", value = "微信", required = false, paramType = "form"),
            @ApiImplicitParam(name = "expertise", value = "业务专长", required = false, paramType = "form"),
            @ApiImplicitParam(name = "jobTitle", value = "职称", required = false, paramType = "form"),
            @ApiImplicitParam(name = "serviceArea", value = "服务地区", required = false, paramType = "form"),
            @ApiImplicitParam(name = "introduction", value = "简介", required = false, paramType = "form"),
            @ApiImplicitParam(name = "file", value = "文件(单图)", required = false, paramType = "form"),

    })
    @PostMapping("/addExpert")
    public ObjectRestResponse<ExpertInfo> add(@RequestParam String expertName,
                                              String phone,
                                              String wechat,
                                              String expertise,
                                              String jobTitle,
                                              String serviceArea,
                                              String introduction,
                                              MultipartFile file) {
        ExpertInfo expertInfo = new ExpertInfo();
        if (!expertName.equals("undefined"))
            expertInfo.setExpertName(expertName);
        if (!phone.equals("undefined"))
            expertInfo.setPhone(phone);
        if (!wechat.equals("undefined"))
            expertInfo.setWechat(wechat);
        if (!expertise.equals("undefined"))
            expertInfo.setExpertise(expertise);
        if (!jobTitle.equals("undefined"))
            expertInfo.setJobTitle(jobTitle);
        if (!serviceArea.equals("undefined"))
            expertInfo.setServiceArea(serviceArea);
        if (!introduction.equals("undefined"))
            expertInfo.setIntroduction(introduction);
        return expertInfoBiz.add(expertInfo, file);
    }

    @ApiOperation(value = "修改专家信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expertName", value = "名称", required = true, paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "电话", required = false, paramType = "form"),
            @ApiImplicitParam(name = "wechat", value = "微信", required = false, paramType = "form"),
            @ApiImplicitParam(name = "expertise", value = "业务专长", required = false, paramType = "form"),
            @ApiImplicitParam(name = "jobTitle", value = "职称", required = false, paramType = "form"),
            @ApiImplicitParam(name = "serviceArea", value = "服务地区", required = false, paramType = "form"),
            @ApiImplicitParam(name = "introduction", value = "简介", required = false, paramType = "form"),
            @ApiImplicitParam(name = "file", value = "文件(单图)", required = false, paramType = "form"),
    })
    @PostMapping("/updateExpert")
    public ObjectRestResponse<ExpertInfo> update(@RequestParam Integer id,
                                                 String expertName,
                                                 String phone,
                                                 String wechat,
                                                 String expertise,
                                                 String jobTitle,
                                                 String serviceArea,
                                                 String introduction,
                                                 MultipartFile file) {
        ExpertInfo expertInfo = new ExpertInfo();
        expertInfo.setId(id);
        expertInfo.setExpertName(expertName);
        expertInfo.setPhone(phone);
        expertInfo.setWechat(wechat);
        expertInfo.setExpertise(expertise);
        expertInfo.setJobTitle(jobTitle);
        expertInfo.setServiceArea(serviceArea);
        expertInfo.setIntroduction(introduction);
        return expertInfoBiz.update(expertInfo, file);
    }

    @ApiOperation(value = "批量删除&单个删除，传List<Integer>")
    @GetMapping("/del")
    public ObjectRestResponse<T> delOne(@RequestParam List<Integer> ids) {
        return expertInfoBiz.delOne(ids);
    }
}
