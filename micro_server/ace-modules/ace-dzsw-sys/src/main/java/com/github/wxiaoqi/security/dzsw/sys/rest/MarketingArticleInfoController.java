package com.github.wxiaoqi.security.dzsw.sys.rest;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.dzsw.sys.biz.MarketingArticleInfoBiz;
import com.github.wxiaoqi.security.dzsw.sys.entity.MarketingArticleInfo;
import com.github.wxiaoqi.security.dzsw.sys.utils.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = {"市场行情/产销信息"})
@RestController
@RequestMapping("/marketing")
@ResponseBody
public class MarketingArticleInfoController extends BaseController<MarketingArticleInfoBiz, MarketingArticleInfo> {
    @Autowired
    protected MarketingArticleInfoBiz marketingArticleInfoBiz;

    @ApiOperation(notes = "查看一条市场行情/产销信息", value = "查看一条市场行情/产销信息")
    @RequestMapping(value = "/getInfoById", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse getInfoById(@RequestParam int id) {
        return marketingArticleInfoBiz.getInfoById(id);
    }

    @ApiOperation(notes = "按浏览量排序显示指定个数的浏览量最高的市场行情/产销信息", value = "按浏览量排序显示指定个数的浏览量最高的市场行情/产销信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", required = true, value = "限制的个数", paramType = "query"),
            @ApiImplicitParam(name = "type", required = true, value = "市场行情/产销信息", paramType = "query"),
    })
    @RequestMapping(value = "/listMostPopularityMarketing", method = RequestMethod.POST)
    public ObjectRestResponse listMostPopularityMarketing(HttpServletRequest request) {
        JSONObject jsonObject = CommonUtil.request2Json(request);
        CommonUtil.hasAllRequired(jsonObject, "limit,type");
        return marketingArticleInfoBiz.getMostPopularityMarketing(jsonObject);
    }

    @ApiOperation(notes = "更新市场行情/产销信息", value = "更新市场行情/产销信息")
    @RequestMapping(value = "/updateInfoById", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse updateInfoById(@RequestBody MarketingArticleInfo marketingArticleInfo) throws Exception {
        return marketingArticleInfoBiz.updateInfoById(marketingArticleInfo);
    }

//    @ApiOperation(notes = "按农产品类型查询市场行情，如果不传农产品分类，则查全部分类。如果不传status（已发布/未发布），则默认查询已发布", value = "listByType")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "pageRow", value = "每页个数", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "category", value = "农产品分类", paramType = "query"),
//            @ApiImplicitParam(name = "status", value = "是否已发布", paramType = "query"),
//    })
//    @RequestMapping(value = "/listByType", method = RequestMethod.POST)
//    public TableResultResponse listByType(HttpServletRequest request) {
//        JSONObject jsonObject = CommonUtil.request2Json(request);
//        CommonUtil.hasAllRequired(jsonObject, "pageNum,pageRow");
//        return marketingArticleInfoBiz.listByType(jsonObject);
//    }

    /**
     * 分页获取产销信息或者市场行情
     *
     * @return
     */
    @ApiOperation(notes = "分页获取产销信息或者市场行情", value = "分页获取产销信息或者市场行情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, paramType = "query"),
            @ApiImplicitParam(name = "type", value = "市场行情/产销信息(=)", required = true, paramType = "query"),
            @ApiImplicitParam(name = "isContainContent", value = "是否包含内容（默认false）", required = false, paramType = "query",dataType = "boolean")
    })
    @RequestMapping(value = "/listByPage", method = RequestMethod.GET)
    public ObjectRestResponse listByPage(@RequestParam Map<String, Object> map) {
        return marketingArticleInfoBiz.listByPage(map);
    }

    @ApiOperation(value = "修改市场行情/产销信息表的内容中网络图片变为本地图片，a标签跳转url变为空")
    @PutMapping("/updateAllContent")
    public ObjectRestResponse<Integer> updateAllArticleContent() {
        return new ObjectRestResponse<>(StatusCode.SUCCESS,marketingArticleInfoBiz.updateAllContent());
    }
}
