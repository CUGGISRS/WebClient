package com.github.wxiaoqi.security.info.sys.rest;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.info.sys.biz.AuthBiz;
import com.github.wxiaoqi.security.info.sys.entity.AuthInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 三品一标认证
 * @author: gsy
 * @create: 2020-09-14 17:13
 **/
@Api(tags = {"三品一标认证"})
@RequestMapping("/productAuth")
@RestController
public class AuthController {

    @Resource
    private AuthBiz authBiz;

    @ApiOperation(value = "新增三品一标认证")
    @PostMapping(value = "/add")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authName", value = "认证名称", required = true, paramType = "form"),
            @ApiImplicitParam(name = "productId", value = "农产品id", required = true, paramType = "form"),
            @ApiImplicitParam(name = "number", value = "认证编号", required = false, paramType = "form"),
            @ApiImplicitParam(name = "institution", value = "认证机构", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authDate", value = "认证时间(date类型)", required = false, paramType = "form"),
            @ApiImplicitParam(name = "pastDate", value = "过期时间(date类型)", required = false, paramType = "form"),
    })
    public ObjectRestResponse<T> addProduct(@RequestParam String authName,
                                            @RequestParam Integer productId,
                                            String number,
                                            String institution,
                                            String authDate,
                                            String pastDate,
                                            MultipartFile[] files) {
        AuthInfo authInfo = new AuthInfo();
        authInfo.setAuthName(authName);
        authInfo.setNumber(number);
        authInfo.setInstitution(institution);
        authInfo.setAuthDate(authDate);
        authInfo.setPastDate(pastDate);
        //农产品id
        authInfo.setProductsId(productId);
        return authBiz.addAuthProduct(authInfo, files);
    }

    @ApiOperation(value = "删除三品一标认证")
    @DeleteMapping("/del/")
    public ObjectRestResponse<T> del(@RequestParam ArrayList<Integer> id) {
        return authBiz.del(id);
    }

    @ApiOperation(value = "修改三品一标认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "认证id", required = true, paramType = "form"),
            @ApiImplicitParam(name = "authName", value = "认证名称", required = true, paramType = "form"),
            @ApiImplicitParam(name = "productId", value = "农产品id", required = true, paramType = "form"),
            @ApiImplicitParam(name = "number", value = "认证编号", required = false, paramType = "form"),
            @ApiImplicitParam(name = "institution", value = "认证机构", required = false, paramType = "form"),
            @ApiImplicitParam(name = "authDate", value = "认证时间(date类型)", required = false, paramType = "form"),
            @ApiImplicitParam(name = "pastDate", value = "过期时间(date类型)", required = false, paramType = "form"),
    })
    @PostMapping("/update")
    public ObjectRestResponse<T> update(
            @RequestParam Integer id,
            String authName,
            Integer productId,
            String number,
            String institution,
            String authDate,
            String pastDate,
            Integer[] delIds,
            MultipartFile[] files) {
        AuthInfo authInfo = new AuthInfo();
        authInfo.setId(id);
        authInfo.setAuthName(authName);
        authInfo.setProductsId(productId);
        authInfo.setNumber(number);
        authInfo.setInstitution(institution);
        authInfo.setAuthDate(authDate);
        authInfo.setPastDate(pastDate);
        return authBiz.update(authInfo, delIds, files);
    }

    @ApiOperation(value = "根据农产品id，查询该三品一标认证详情,[多个认证信息]")
    @GetMapping("/getByProductId")
    public ObjectRestResponse<List<JSONObject>> getByProductId(@RequestParam Integer productId) {
        return authBiz.getByProductId(productId);
    }

    @ApiOperation(value = "根据三品一标认证id，查询该三品一标认证详情")
    @GetMapping("/getByAuthId")
    public ObjectRestResponse<T> getByAuthId(@RequestParam Integer AuthId) {
        return authBiz.getByAuthId(AuthId);
    }

    //    @ApiOperation(value = "分页查询,认证信息貌似不需要，暂未完成")
//    @GetMapping("/page")
    public ObjectRestResponse<T> query(@RequestParam Integer page, @RequestParam Integer size) {
        return authBiz.query(page, size);
    }

}
