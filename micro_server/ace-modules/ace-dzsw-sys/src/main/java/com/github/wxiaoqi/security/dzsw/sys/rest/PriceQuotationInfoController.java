package com.github.wxiaoqi.security.dzsw.sys.rest;

import cn.hutool.core.util.PageUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.dzsw.sys.biz.PriceQuotationInfoBiz;
import com.github.wxiaoqi.security.dzsw.sys.entity.PriceQuotationInfo;
import com.github.wxiaoqi.security.dzsw.sys.utils.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author TsaiJun
 */
@Api(tags = {"价格行情"})
@RestController
@RequestMapping("/price")
@ResponseBody
public class PriceQuotationInfoController extends BaseController<PriceQuotationInfoBiz, PriceQuotationInfo> {
    @Autowired
    protected PriceQuotationInfoBiz priceQuotationInfoBiz;

    @RequestMapping(value = "/listByPage", method = RequestMethod.GET)
    public ObjectRestResponse listByPage(@RequestParam Map<String, Object> map) {
        return priceQuotationInfoBiz.listByPage(map);
    }

    @ApiOperation(notes = "按农产品类型和时间查询价格行情", value = "按农产品类型和时间查询价格行情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageRow", value = "每页个数", required = true, paramType = "query"),
            @ApiImplicitParam(name = "category", value = "农产品分类", paramType = "query"),
            @ApiImplicitParam(name = "time", value = "时间段", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/listByTypeAndTime", method = RequestMethod.POST)
    public ObjectRestResponse listByTypeAndTime(HttpServletRequest request) {
        JSONObject jsonObject = CommonUtil.request2Json(request);
        CommonUtil.hasAllRequired(jsonObject, "pageNum,pageRow,time");
        return priceQuotationInfoBiz.listByTypeAndTime(jsonObject);
    }

    @ApiOperation(notes = "按报价时间最近随机显示指定个数的价格行情", value = "按报价时间最近随机显示指定个数的价格行情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "限制的个数", paramType = "query"),
    })
    @RequestMapping(value = "/listRecentPriceInfo", method = RequestMethod.POST)
    public ObjectRestResponse listRecentPriceInfo(HttpServletRequest request) {
        JSONObject jsonObject = CommonUtil.request2Json(request);
        return priceQuotationInfoBiz.listRecentPriceInfo(jsonObject);
    }

    // 价格走势分析，根据所选农产品分类，查询某一农产品在所选年度内的价格走势。
    @ApiOperation(notes = "价格走势分析，根据所选农产品分类，查询某一农产品在所选年度内的价格走势", value = "价格走势分析，根据所选农产品分类，查询某一农产品在所选年度内的价格走势")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value = "农产品分类", required = true, paramType = "query"),
            @ApiImplicitParam(name = "product", value = "农产品名称", required = true, paramType = "query"),
            @ApiImplicitParam(name = "year", value = "所选年份", required = true, paramType = "query")
    })
    @RequestMapping(value = "/listByProductAndTime", method = RequestMethod.POST)
    public ObjectRestResponse listByProductAndTime(HttpServletRequest request) {
        JSONObject jsonObject = CommonUtil.request2Json(request);
        CommonUtil.hasAllRequired(jsonObject, "category,product,year");
        return priceQuotationInfoBiz.listByProductAndTime(jsonObject);
    }

    @ApiOperation(value = "分页查询所有可以选择的时间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, paramType = "query"),
    })
    @GetMapping(value = "/getAvailableDate")
    public ObjectRestResponse<Map<String, Object>> getAvailableDate(@RequestParam int page, @RequestParam int limit) {
        int[] ints = PageUtil.transToStartEnd(page - 1, limit);
        ints[1] = limit;
        return priceQuotationInfoBiz.getAvailableDate(ints);
    }

    @ApiOperation(value = "查询某一类农产品所有产品的价格列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value = "农产品分类", required = true, paramType = "query"),
    })
    @GetMapping(value = "/getPriceListByCategory")
    public ObjectRestResponse getPriceListByCategory(@RequestParam String category) {
        return priceQuotationInfoBiz.getPriceListByCategory(category);
    }

    @ApiOperation(value = "通过日期，查询所有的产品价格")
    @GetMapping(value = "/getPriceListByOneDate")
    public List<Object> getPriceListByOneDate(@RequestParam String date) {
        return priceQuotationInfoBiz.getPriceListByOneDate(date);
    }

    @ApiOperation(value = "通过Excel导入价格行情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "Excel文件", required = true, paramType = "form"),
    })
    @PostMapping(value = "/importByExcel")
    public ObjectRestResponse importByExcel(@RequestParam MultipartFile file) {
        return priceQuotationInfoBiz.importByExcel(file);
    }


    @ApiOperation(value = "统计某一报价时间段的各乡镇各农产品类型的产品种类数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "offerTimeS", value = "报价时间起点(>=)",  paramType = "form",dataType = "Date"),
            @ApiImplicitParam(name = "offerTimeE", value = "报价时间终点(<=)",  paramType = "form",dataType = "Date")
    })
    @PostMapping(value = "/statisticsProductNum")
    public ObjectRestResponse statisticsProductNum(Date offerTimeS, Date offerTimeE) {
        return priceQuotationInfoBiz.statisticsProductNum(offerTimeS,offerTimeE);
    }


}
