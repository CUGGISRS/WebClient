package com.github.wxiaoqi.security.dzsw.sys.rest;

import cn.hutool.core.util.PageUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.dzsw.sys.biz.QualityProductsInfoBiz;
import com.github.wxiaoqi.security.dzsw.sys.entity.QualityProductsInfo;
import com.github.wxiaoqi.security.dzsw.sys.utils.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = {"优质农产品"})
@RestController
@RequestMapping("/perfectProduct")
@ResponseBody
public class QualityProductsInfoController extends BaseController<QualityProductsInfoBiz, QualityProductsInfo> {

    @Autowired
    protected QualityProductsInfoBiz qualityProductsInfoBiz;

    @ApiOperation(notes = "查看一条优质农产品", value = "查看一条优质农产品")
    @RequestMapping(value = "/getInfoById", method = RequestMethod.GET)
    @ResponseBody
    /**
     * 为当前优质农产品热度加1
     */
    public ObjectRestResponse getInfoById(@RequestParam int id) {
        return qualityProductsInfoBiz.getInfoById(id);
    }

    @ApiOperation(notes = "按热度排序显示指定个数的优质农产品", value = "按热度排序显示指定个数的优质农产品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "限制的个数", paramType = "query"),
    })
    @RequestMapping(value = "/listMostPopularityProduct", method = RequestMethod.POST)
    public ObjectRestResponse listMostPopularityProduct(HttpServletRequest request) {
        JSONObject jsonObject = CommonUtil.request2Json(request);
        return qualityProductsInfoBiz.getMostPopularityProduct(jsonObject);
    }

//    @ApiOperation(notes = "按农产品类型查询优质农产品，如果不传农产品分类，则查全部分类。如果不传status（审核通过/待审核），则默认查询已发布", value = "listByType")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "pageRow", value = "每页个数", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "category", value = "农产品分类", paramType = "query"),
//            @ApiImplicitParam(name = "status", value = "是否审核通过", paramType = "query"),
//    })
//    @RequestMapping(value = "/listByType", method = RequestMethod.POST)
//    public TableResultResponse listByType(HttpServletRequest request) {
//        JSONObject jsonObject = CommonUtil.request2Json(request);
//        CommonUtil.hasAllRequired(jsonObject, "pageNum,pageRow");
//        return qualityProductsInfoBiz.listByType(jsonObject);
//    }

    @ApiOperation(value = "新增优质农产品", notes = "使用postman测试接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productName", value = "优质农产品名称", required = true, paramType = "form"),
            @ApiImplicitParam(name = "productCategory", value = "优质农产品分类", required = false, paramType = "form"),
            @ApiImplicitParam(name = "price", value = "价格", required = false, paramType = "form"),
            @ApiImplicitParam(name = "description", value = "描述", required = false, paramType = "form"),
            @ApiImplicitParam(name = "productOrigin", value = "产品产地", required = false, paramType = "form"),
            @ApiImplicitParam(name = "purchaseWay", value = "购买方式", required = false, paramType = "form"),
            @ApiImplicitParam(name = "status", value = "发布状态", required = false, paramType = "form"),
            @ApiImplicitParam(name = "referrerid", value = "发布人id", required = false, paramType = "form"),
            @ApiImplicitParam(name = "referrer", value = "发布人", required = false, paramType = "form"),
            @ApiImplicitParam(name = "files", value = "文件数组", required = false, paramType = "form"),
    })
    @PostMapping("/add")
    public ObjectRestResponse add(@RequestParam Map<String, String> map, @RequestParam MultipartFile[] files) {
        return qualityProductsInfoBiz.addNewQualityProduct(map, files);
    }


    /**
     * 分页展示优质农产品
     *
     * @return
     */
    @ApiOperation(notes = "按农产品类型查询优质农产品，如果不传农产品分类，则查全部分类。如果不传status（审核通过/待审核），则默认查询已发布", value = "按农产品类型查询优质农产品，如果不传农产品分类，则查全部分类。如果不传status（审核通过/待审核），则默认查询已发布")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/listByPage", method = RequestMethod.GET)
    public ObjectRestResponse listByPage(@RequestParam Integer page, @RequestParam Integer limit, @RequestParam Map<String, Object> params) {
        int[] ints = PageUtil.transToStartEnd(page - 1, limit);
        ints[1] = limit;
        return qualityProductsInfoBiz.listByPage(ints, params);
    }

    /**
     * 更新优质农产品
     *
     * @param map
     * @return
     */

    @ApiImplicitParams({
            @ApiImplicitParam(name = "productName", value = "优质农产品名称", required = true, paramType = "form"),
            @ApiImplicitParam(name = "productCategory", value = "优质农产品分类", required = false, paramType = "form"),
            @ApiImplicitParam(name = "price", value = "价格", required = false, paramType = "form"),
            @ApiImplicitParam(name = "description", value = "描述", required = false, paramType = "form"),
            @ApiImplicitParam(name = "productOrigin", value = "产品产地", required = false, paramType = "form"),
            @ApiImplicitParam(name = "purchaseWay", value = "购买方式", required = false, paramType = "form"),
            @ApiImplicitParam(name = "status", value = "发布状态", required = false, paramType = "form"),
            @ApiImplicitParam(name = "referrerid", value = "发布人id", required = false, paramType = "form"),
            @ApiImplicitParam(name = "referrer", value = "发布人", required = false, paramType = "form"),
            @ApiImplicitParam(name = "delIds", value = "删除的图片id数组", required = false, paramType = "form"),
            @ApiImplicitParam(name = "files", value = "文件数组", required = false, paramType = "form"),
    })
    @RequestMapping(value = "/updateQualityProduct", method = RequestMethod.POST)
    public ObjectRestResponse updateQualityProduct(@RequestParam Map<String, String> map, Integer[] delIds,
                                                   MultipartFile[] files) {
        return qualityProductsInfoBiz.updateQualityProduct(map, delIds, files);
    }

    @ApiOperation(value = "批量删除&单个删除，传List<Integer>")
    @GetMapping("/del")
    public ObjectRestResponse<T> delOne(@RequestParam List<Integer> ids) {
        return qualityProductsInfoBiz.delOne(ids);
    }

    @RequestMapping(value = "/abc", method = RequestMethod.POST)
    public String demo() {
        return "测试";
    }
}
